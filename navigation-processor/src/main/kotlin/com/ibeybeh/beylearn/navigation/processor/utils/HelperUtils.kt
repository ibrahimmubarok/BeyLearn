package com.ibeybeh.beylearn.navigation.processor.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * Membangun blok kode `typeMap = mapOf(...)` dengan format indentasi KotlinPoet
 * \n = Enter, ⇥ = Indent Kanan, ⇤ = Indent Kiri
 */
internal fun createTypeMapCodeBlock(
    customParams: List<ClassName>,
    typeMapOfMember: MemberName
): CodeBlock? {
    if (customParams.isEmpty()) return null

    return CodeBlock.builder().apply {
        add("\n⇥typeMap = mapOf(\n⇥")
        customParams.forEachIndexed { index, paramClass ->
            add("%M<%T>()", typeMapOfMember, paramClass)
            if (index < customParams.lastIndex) {
                add(",\n")
            } else {
                add("\n")
            }
        }
        add("⇤)\n⇤")
    }.build()
}

internal fun getParentGraphType(func: KSFunctionDeclaration): KSType {
    val annotation = func.annotations.find { it.shortName.asString() == "BeyDestination" }!!
    val arg = annotation.arguments.find { it.name?.asString() == "parentGraph" }
        ?: annotation.arguments.getOrNull(1)
    return arg?.value as? KSType
        ?: throw IllegalStateException("parentGraph tidak ditemukan di ${func.simpleName.asString()}")
}

internal fun getRouteKSType(func: KSFunctionDeclaration): KSType {
    val annotation = func.annotations.find { it.shortName.asString() == "BeyDestination" }!!
    val arg = annotation.arguments.find { it.name?.asString() == "route" }
        ?: annotation.arguments.getOrNull(0)
    return arg?.value as? KSType
        ?: throw IllegalStateException("Route class tidak ditemukan di ${func.simpleName.asString()}")
}

internal fun isStartDestination(func: KSFunctionDeclaration): Boolean {
    val annotation =
        func.annotations.find { it.shortName.asString() == "BeyDestination" } ?: return false
    val arg = annotation.arguments.find { it.name?.asString() == "isStartDestination" }
    val value = arg?.value ?: annotation.arguments.getOrNull(2)?.value
    return value as? Boolean ?: false
}

/**
 * Membaca `primaryConstructor` dari data class NavArgs,
 * lalu mengembalikan daftar parameter yang bukan tipe data primitif
 */
internal fun getCustomRouteParameters(routeType: KSType): List<ClassName> {
    val routeDecl = routeType.declaration as? KSClassDeclaration ?: return emptyList()

    // Tipe bawaan Kotlin yang sudah di-handle oleh Navigation Compose bawaan
    val primitiveTypes = setOf(
        "String", "Int", "Boolean", "Float", "Long", "Double",
        "String?", "Int?", "Boolean?", "Float?", "Long?", "Double?",
        "IntArray", "BooleanArray", "FloatArray", "LongArray", "DoubleArray"
    )

    return routeDecl.primaryConstructor?.parameters?.mapNotNull { param ->
        val resolvedType = param.type.resolve()
        val typeName = resolvedType.declaration.simpleName.asString()

        // Jika tipe (contoh 'Profile') tidak ada di set primitiveTypes, berarti dia custom object
        if (typeName !in primitiveTypes) {
            resolvedType.toClassName()
        } else {
            null
        }
    } ?: emptyList()
}