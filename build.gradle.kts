// This is an example of a lifecycle task that crosses build boundaries defined in the umbrella build.
tasks.register("checkFeatures") {
    group = "verification"
    description = "Run all feature tests"
    dependsOn(gradle.includedBuild("book-catalog-service").task(":check"))
}

tasks.register("cleanGeneratedApis") {
    group = "build"
    description = "Clean all generated OpenAPI resources"
    dependsOn(gradle.includedBuild("book-catalog-service").task(":apis:server:clean"))
}

tasks.register("runConfigService") {
    group = "localTasks"
    description = "Run Config Service"
    dependsOn(gradle.includedBuild("book-catalog-config-service").task(":bootRun"))
}