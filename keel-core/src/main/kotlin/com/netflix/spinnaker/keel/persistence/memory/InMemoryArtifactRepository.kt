package com.netflix.spinnaker.keel.persistence.memory

import com.netflix.spinnaker.keel.api.ArtifactType
import com.netflix.spinnaker.keel.api.DeliveryArtifact
import com.netflix.spinnaker.keel.api.DeliveryConfig
import com.netflix.spinnaker.keel.persistence.ArtifactAlreadyRegistered
import com.netflix.spinnaker.keel.persistence.ArtifactRepository
import com.netflix.spinnaker.keel.persistence.NoSuchArtifactException

class InMemoryArtifactRepository : ArtifactRepository {
  private val artifacts = mutableMapOf<DeliveryArtifact, MutableList<String>>()
  private val approvedVersions = mutableMapOf<Triple<DeliveryArtifact, DeliveryConfig, String>, String>()
  private val deployedVersions = mutableMapOf<Triple<DeliveryArtifact, DeliveryConfig, String>, MutableList<String>>()

  override fun register(artifact: DeliveryArtifact) {
    if (artifacts.containsKey(artifact)) {
      throw ArtifactAlreadyRegistered(artifact)
    }
    artifacts[artifact] = mutableListOf()
  }

  override fun store(artifact: DeliveryArtifact, version: String): Boolean {
    if (!artifacts.containsKey(artifact)) {
      throw NoSuchArtifactException(artifact)
    }
    val versions = artifacts[artifact] ?: throw IllegalArgumentException()
    return if (versions.none { it == version }) {
      versions.add(0, version)
      true
    } else {
      false
    }
  }

  override fun isRegistered(name: String, type: ArtifactType) =
    artifacts.keys.any {
      it.name == name && it.type == type
    }

  override fun versions(artifact: DeliveryArtifact): List<String> =
    artifacts[artifact] ?: throw NoSuchArtifactException(artifact)

  override fun approveVersionFor(
    deliveryConfig: DeliveryConfig,
    artifact: DeliveryArtifact,
    version: String,
    targetEnvironment: String
  ) {
    val key = Triple(artifact, deliveryConfig, targetEnvironment)
    approvedVersions[key] = version
  }

  override fun latestVersionApprovedIn(
    deliveryConfig: DeliveryConfig,
    artifact: DeliveryArtifact,
    targetEnvironment: String
  ): String? {
    val key = Triple(artifact, deliveryConfig, targetEnvironment)
    return approvedVersions[key]
  }

  override fun wasSuccessfullyDeployedTo(
    deliveryConfig: DeliveryConfig,
    artifact: DeliveryArtifact,
    version: String,
    targetEnvironment: String
  ): Boolean {
    val key = Triple(artifact, deliveryConfig, targetEnvironment)
    return deployedVersions[key]?.contains(version) ?: false
  }

  override fun markAsSuccessfullyDeployedTo(
    deliveryConfig: DeliveryConfig,
    artifact: DeliveryArtifact,
    version: String,
    targetEnvironment: String
  ) {
    val key = Triple(artifact, deliveryConfig, targetEnvironment)
    val list = deployedVersions[key]
    if (list == null) {
      deployedVersions[key] = mutableListOf(version)
    } else {
      list.add(version)
    }
  }

  fun dropAll() {
    artifacts.clear()
    approvedVersions.clear()
    deployedVersions.clear()
  }
}
