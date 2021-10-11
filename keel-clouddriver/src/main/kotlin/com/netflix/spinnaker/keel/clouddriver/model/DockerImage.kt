/*
 *
 * Copyright 2019 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.netflix.spinnaker.keel.clouddriver.model

data class DockerImage(
  val account: String,
  val repository: String,
  val tag: String,
  val digest: String?,
  val artifact: ArtifactProperty? = null,
  val registry: String? = null,
  val buildNumber: String? = null,
  val commitId: String? = null,
  val prCommitId: String? = null,
  val branch: String? = null,
  val date: String? = null
)

data class ArtifactProperty(
  val metadata: Map<String, Any?>,
  val name: String,
  val reference: String,
  val type: String,
  val version: String
)
