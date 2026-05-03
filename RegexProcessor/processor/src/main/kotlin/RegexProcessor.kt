package com.dam

import annotations.Extract
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class) // Regista o processador automaticamente
@SupportedAnnotationTypes("annotations.Extract")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
class RegexProcessor : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        // 1. Encontrar todos os elementos anotados com @Extract
        val annotatedElements = roundEnv.getElementsAnnotatedWith(Extract::class.java)
        if (annotatedElements.isEmpty()) return false

        // 2. Agrupar por classe (porque um DataProcessor pode ter vários métodos @Extract)
        val groupedByClass = annotatedElements.groupBy { it.enclosingElement as TypeElement }

        groupedByClass.forEach { (classElement, methods) ->
            generateExtractorClass(classElement, methods)
        }

        return true
    }

    private fun generateExtractorClass(classElement: TypeElement, methods: List<Element>) {
        val packageName = processingEnv.elementUtils.getPackageOf(classElement).toString()
        val originalClassName = classElement.simpleName.toString()
        val generatedClassName = "${originalClassName}Extractor"

        // Criar a classe que herda da original
        val classBuilder = TypeSpec.classBuilder(generatedClassName)
            .superclass(classElement.asType().asTypeName())
            .addModifiers(KModifier.PUBLIC)
            // Adicionar construtor que recebe 'input' e passa para a super classe
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("input", String::class)
                    .build()
            )

        // No teu caso, como a classe abstrata recebe (val input: String),
        // precisamos de garantir que passamos esse parâmetro para o super()
        classBuilder.addSuperclassConstructorParameter("input")

        // 3. Implementar cada método anotado
        methods.forEach { method ->
            val executableMethod = method as ExecutableElement
            val methodName = executableMethod.simpleName.toString()
            val regexValue = method.getAnnotation(Extract::class.java).regex

            val methodBuilder = FunSpec.builder(methodName)
                .addModifiers(KModifier.OVERRIDE)
                .returns(String::class.asTypeName().copy(nullable = true))
                .addCode(
                    """
                    val match = Regex(%S).find(input)
                    return match?.groupValues?.get(1)
                    """.trimIndent(), regexValue
                )

            classBuilder.addFunction(methodBuilder.build())
        }

        // 4. Escrever o ficheiro
        val file = FileSpec.builder(packageName, generatedClassName)
            .addType(classBuilder.build())
            .build()

        file.writeTo(processingEnv.filer)
    }
}