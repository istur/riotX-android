/*
 * Copyright 2018 New Vector Ltd
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

package im.vector.matrix.android.internal.crypto.keysbackup.model.rest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import im.vector.matrix.android.api.util.JsonDict

@JsonClass(generateAdapter = true)
data class CreateKeysBackupVersionBody(
        /**
         * The algorithm used for storing backups. Currently, only "m.megolm_backup.v1.curve25519-aes-sha2" is defined
         */
        @Json(name = "algorithm")
        override val algorithm: String? = null,

        /**
         * algorithm-dependent data, for "m.megolm_backup.v1.curve25519-aes-sha2"
         * see [im.vector.matrix.android.internal.crypto.keysbackup.MegolmBackupAuthData]
         */
        @Json(name = "auth_data")
        override val authData: JsonDict? = null
) : KeysAlgorithmAndData
