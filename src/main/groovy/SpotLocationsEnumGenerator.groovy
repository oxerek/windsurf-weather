import com.sonalake.windsurfweather.domain.ports.dto.SpotDto
import com.squareup.javapoet.*
import io.vavr.control.Try
import lombok.AccessLevel
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.experimental.Accessors
import lombok.experimental.FieldDefaults
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser

import javax.lang.model.element.Modifier
import java.nio.file.Files
import java.nio.file.Paths
import java.time.ZoneId
import java.util.function.Supplier

class SpotLocationsEnumGenerator {

    static void main(String[] args) {

        def enumName = "SpotLocations"
        def enumPackage = "com.sonalake.windsurfweather.domain"
        def srcFolder = "src/main/java"

        def csvReader = Try.withResources(() -> new CSVParser(
                Files.newBufferedReader(Paths.get("spot-locations.csv")),
                CSVFormat.DEFAULT.withFirstRecordAsHeader()));

        def enumSpecBuilder = TypeSpec
                .enumBuilder(enumName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Getter.class).build())
                .addAnnotation(AnnotationSpec.builder(Accessors.class)
                        .addMember("fluent", "true")
                        .build())
                .addAnnotation(AnnotationSpec.builder(FieldDefaults.class)
                        .addMember("makeFinal", "true")
                        .addMember("level", "\$T.\$L", AccessLevel.class, AccessLevel.PRIVATE.name())
                        .build())
                .addAnnotation(RequiredArgsConstructor.class)
                .addField(String.class, "cityName")
                .addField(String.class, "countryCode")
                .addField(BigDecimal.class, "lon")
                .addField(BigDecimal.class, "lat")
                .addField(ZoneId.class, "timezone")
                .addMethod(MethodSpec.methodBuilder("of")
                        .addModifiers(Modifier.STATIC)
                        .addParameter(String.class, "cityName")
                        .addParameter(String.class, "countryCode")
                        .returns(ParameterizedTypeName.get(
                                ClassName.get(Supplier.class) as ClassName,
                                ParameterizedTypeName.get(ClassName.get(Optional.class) as ClassName, ClassName.bestGuess(enumName) as TypeName) as TypeName
                        ) as TypeName)
                        .addStatement("return () -> java.util.stream.Stream.of(values())\n" +
                                ".filter(location -> location.cityName.equalsIgnoreCase(cityName) && location.countryCode.equalsIgnoreCase(countryCode))\n" +
                                ".findFirst()")
                        .build())
                .addMethod(MethodSpec.methodBuilder("toDto")
                        .returns(ParameterizedTypeName.get(ClassName.get(Supplier.class) as ClassName, ClassName.get(SpotDto.class) as TypeName) as TypeName)
                        .addStatement("return () -> new SpotDto(cityName(), countryCode())")
                        .build())

        csvReader.of(records -> records.getRecords().stream())
                .get()
                .forEach(record -> enumSpecBuilder.addEnumConstant(
                        record.get("enumName"), TypeSpec.anonymousClassBuilder(
                        "\$S, \$S, new BigDecimal(\$S), new BigDecimal(\$S), ZoneId.of(\$S)",
                        record.get("cityName"), record.get("countryCode"), record.get("lon"), record.get("lat"), record.get("timezone")
                ).build()).build())


        def javaFile = JavaFile
                .builder(enumPackage, enumSpecBuilder.build())
                .indent("    ")
                .build()

        javaFile.writeTo(Paths.get(srcFolder))
    }
}