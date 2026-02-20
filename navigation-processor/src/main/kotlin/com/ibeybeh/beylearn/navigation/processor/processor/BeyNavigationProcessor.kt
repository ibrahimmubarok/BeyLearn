package com.ibeybeh.beylearn.navigation.processor.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.ibeybeh.beylearn.navigation.processor.utils.createTypeMapCodeBlock
import com.ibeybeh.beylearn.navigation.processor.utils.getCustomRouteParameters
import com.ibeybeh.beylearn.navigation.processor.utils.getParentGraphType
import com.ibeybeh.beylearn.navigation.processor.utils.getRouteKSType
import com.ibeybeh.beylearn.navigation.processor.utils.isStartDestination
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class BeyNavigationProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    companion object {
        // Sesuaikan dengan lokasi anotasi Anda
        private const val ANNOTATION_QNAME =
            "com.ibeybeh.beylearn.core_navigation.annotation.BeyDestination"

        // Sesuaikan dengan package tempat Anda menyimpan inline fun typeMapOf()
        private const val TYPE_MAP_OF_PACKAGE =
            "com.ibeybeh.beylearn.core_navigation.util"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(ANNOTATION_QNAME)
        val validFunctions = symbols.filterIsInstance<KSFunctionDeclaration>()
            .filter { it.validate() }

        if (!validFunctions.iterator().hasNext()) return emptyList()

        try {
            // 1. Grouping route berdasarkan Parent Graph-nya
            val graphGroups = validFunctions.groupBy { func ->
                getParentGraphType(func).toClassName()
            }

            graphGroups.forEach { (graphClassName, routes) ->
                // 2. Generate ekstensi NavGraphBuilder (Contoh: homeNavGraph)
                generateGraphExtension(resolver, graphClassName, routes)

                // 3. Generate ekstensi SavedStateHandle untuk SETIAP route dalam graph tersebut
                routes.forEach { routeFunc ->
                    val routeKSType = getRouteKSType(routeFunc)
                    generateSavedStateHandleExtension(routeKSType, routeFunc)
                }
            }

        } catch (e: Exception) {
            logger.error("Error processing navigation: ${e.message}")
        }

        return symbols.filterNot { it.validate() }.toList()
    }

    // =========================================================================
    // GENERATOR 1: NavGraphBuilder Extension (homeNavGraph)
    // =========================================================================
    private fun generateGraphExtension(
        resolver: Resolver,
        graphClassName: ClassName,
        routes: List<KSFunctionDeclaration>
    ) {
        val navGraphBuilderClass = ClassName("androidx.navigation", "NavGraphBuilder")
        val navManagerClass = ClassName(
            "com.ibeybeh.beylearn.core_navigation.navigator",
            "NavigationManager"
        ) // Sesuaikan dengan package NavigationManager Anda

        val navComposeNavigation = MemberName("androidx.navigation.compose", "navigation")
        val navComposeComposable = MemberName("androidx.navigation.compose", "composable")
        val typeMapOfMember = MemberName(TYPE_MAP_OF_PACKAGE, "typeMapOf")

        val graphName = graphClassName.simpleName.replaceFirstChar { it.lowercase() }
        val startRoute = routes.find { isStartDestination(it) } ?: routes.firstOrNull() ?: return
        val startRouteClass = getRouteKSType(startRoute).toClassName()

        val codeBlock = CodeBlock.builder().apply {
            // navigation<HomeNavGraph>(startDestination = HomeRoute::class)
            beginControlFlow(
                "%M<%T>(startDestination = %T::class)",
                navComposeNavigation,
                graphClassName,
                startRouteClass
            )

            routes.forEach { func ->
                val routeKSType = getRouteKSType(func)
                val routeClass = routeKSType.toClassName()
                val screenFunction =
                    MemberName(func.packageName.asString(), func.simpleName.asString())

                // Inspeksi apakah argumen route punya custom class (seperti Profile)
                val customParams = getCustomRouteParameters(routeKSType)

                // Buat blok `typeMap = mapOf(...)`
                val typeMapBlock = createTypeMapCodeBlock(customParams, typeMapOfMember)

                // Render composable dengan atau tanpa typeMap
                if (typeMapBlock == null) {
                    beginControlFlow("%M<%T>", navComposeComposable, routeClass)
                } else {
                    beginControlFlow("%M<%T>(%L)", navComposeComposable, routeClass, typeMapBlock)
                }

                // Panggil Screen: DetailProfileScreen(navigationManager = navigationManager)
                addStatement("%M(navigationManager = navigationManager)", screenFunction)
                endControlFlow()
            }
            endControlFlow()
        }.build()

        val functionSpec = FunSpec.builder(graphName)
            .addModifiers(KModifier.PUBLIC)
            .receiver(navGraphBuilderClass)
            .addParameter("navigationManager", navManagerClass)
            .addCode(codeBlock)
            .build()

        val fileSpec =
            FileSpec.builder(graphClassName.packageName, "${graphClassName.simpleName}Ext")
                .addFunction(functionSpec)
                .build()

        val dependencies = Dependencies(
            aggregating = true,
            sources = routes.mapNotNull { it.containingFile }.toTypedArray()
        )

        fileSpec.writeTo(codeGenerator, dependencies)
    }

    // =========================================================================
    // GENERATOR 2: SavedStateHandle Extension (getDetailProfileNavArgs)
    // =========================================================================
    private fun generateSavedStateHandleExtension(
        routeKSType: KSType,
        routeFunc: KSFunctionDeclaration
    ) {
        // 1. Ambil deklarasi kelas dari routeKSType
        val classDeclaration = routeKSType.declaration as? KSClassDeclaration

        // 2. Cek apakah constructor utama memiliki parameter
        val hasParameters = classDeclaration?.primaryConstructor?.parameters?.isNotEmpty() == true

        // 3. Jika tidak ada parameter, hentikan fungsi di sini (jangan buat file)
        if (!hasParameters) {
            return
        }

        // --- KODE LAMA KAMU TETAP BERJALAN DI BAWAH SINI ---
        val routeClass = routeKSType.toClassName()
        val customParams = getCustomRouteParameters(routeKSType)

        val savedStateHandleClass = ClassName("androidx.lifecycle", "SavedStateHandle")
        val toRouteMember = MemberName("androidx.navigation", "toRoute")
        val typeMapOfMember = MemberName(TYPE_MAP_OF_PACKAGE, "typeMapOf")

        val extensionName = "get${routeClass.simpleName}"

        val typeMapBlock = createTypeMapCodeBlock(customParams, typeMapOfMember)

        val funSpecBuilder = FunSpec.builder(extensionName)
            .addModifiers(KModifier.INTERNAL)
            .receiver(savedStateHandleClass)
            .returns(routeClass)

        if (typeMapBlock == null) {
            funSpecBuilder.addStatement("return %M<%T>()", toRouteMember, routeClass)
        } else {
            funSpecBuilder.addStatement(
                "return %M<%T>(%L)",
                toRouteMember,
                routeClass,
                typeMapBlock
            )
        }

        // Pastikan package namenya sudah menggunakan .savedstatehandle seperti yang kita diskusikan sebelumnya
        val targetPackage = "${routeClass.packageName}.savedStateExt"

        val fileSpec = FileSpec.builder(targetPackage, "${routeClass.simpleName}SavedStateExt")
            .addFunction(funSpecBuilder.build())
            .build()

        val fileSource = routeFunc.containingFile
        val dependencies = if (fileSource != null) {
            Dependencies(aggregating = true, fileSource)
        } else {
            Dependencies(aggregating = true)
        }

        fileSpec.writeTo(codeGenerator, dependencies)
    }
}