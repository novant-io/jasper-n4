/*
 * Copyright 2024 Novant. All Rights Reserved.
 */

plugins {
  // Base Niagara plugin
  id("com.tridium.niagara")

  // The vendor plugin provides the vendor {} extension to set the default group
  // for Maven publishing; the default vendor attribute for installable
  // manifests; and the default module and dist version for their respective
  // manifests
  id("com.tridium.vendor")

  // The signing plugin configures signing of all executables, modules, and
  // dists. It also registers a factory only on the root project to avoid
  // overhead from managing signing profiles on all subprojects
  id("com.tridium.niagara-signing")

  // The niagara_home repositories convention plugin configures !bin/ext and
  // !modules as flat-file Maven repositories to allow modules to compile against
  // Niagara
  id("com.tridium.convention.niagara-home-repositories")
}


vendor {
  // defaultVendor sets the "vendor" attribute on module and dist files; it's
  // what's shown in Niagara when viewing a module or dist.
  defaultVendor("Novant")

  // defaultModuleVersion sets the "vendorVersion" attribute on all modules
  defaultModuleVersion("0.10")
}

niagaraSigning {

  // load secrets
  var sec = HashMap<String, String>()
  val f = rootProject.file("secrets/signing.secrets")
  if (!f.exists()) throw Exception("ERR: signing secrets not found")
  f.forEachLine {
    if (it[0] != '#')
      sec[it.split("=")[0]] = it.split("=")[1]
  }

  aliases.set(listOf(sec["key_alias"]))
  signingProfileFile.set(
    project.rootProject.layout.projectDirectory.file("niagara.signing.properties")
  )
}

////////////////////////////////////////////////////////////////
// Dependencies and configurations... configuration
////////////////////////////////////////////////////////////////

subprojects {
  repositories {
    mavenCentral()
  }
}
