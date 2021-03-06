/*
 * Copyright (c) 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.matrix.android.internal.session.account

import im.vector.matrix.android.api.failure.toRegistrationFlowResponse
import im.vector.matrix.android.internal.di.UserId
import im.vector.matrix.android.internal.network.executeRequest
import im.vector.matrix.android.internal.task.Task
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

internal interface ChangePasswordTask : Task<ChangePasswordTask.Params, Unit> {
    data class Params(
            val password: String,
            val newPassword: String
    )
}

internal class DefaultChangePasswordTask @Inject constructor(
        private val accountAPI: AccountAPI,
        private val eventBus: EventBus,
        @UserId private val userId: String
) : ChangePasswordTask {

    override suspend fun execute(params: ChangePasswordTask.Params) {
        val changePasswordParams = ChangePasswordParams.create(userId, params.password, params.newPassword)
        try {
            executeRequest<Unit>(eventBus) {
                apiCall = accountAPI.changePassword(changePasswordParams)
            }
        } catch (throwable: Throwable) {
            val registrationFlowResponse = throwable.toRegistrationFlowResponse()

            if (registrationFlowResponse != null
                    /* Avoid infinite loop */
                    && changePasswordParams.auth?.session == null) {
                // Retry with authentication
                executeRequest<Unit>(eventBus) {
                    apiCall = accountAPI.changePassword(
                            changePasswordParams.copy(auth = changePasswordParams.auth?.copy(session = registrationFlowResponse.session))
                    )
                }
            } else {
                throw throwable
            }
        }
    }
}
