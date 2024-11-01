package org.openapitools.codegen.languages;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;
import org.openapitools.codegen.meta.GeneratorMetadata;
import org.openapitools.codegen.meta.Stability;
import org.openapitools.codegen.meta.features.*;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openapitools.codegen.utils.StringUtils.camelize;

public class ScalaSttp4JsoniterClientCodegen extends AbstractScalaCodegen implements CodegenConfig {
    private static final StringProperty STTP_CLIENT_VERSION = new StringProperty("sttpClientVersion",
            "The version of " +
                    "sttp client",
            "4.0.0-M19");
    private static final BooleanProperty USE_SEPARATE_ERROR_CHANNEL = new BooleanProperty("separateErrorChannel",
            "Whether to return response as " +
                    "F[Either[ResponseError[ErrorType], ReturnType]]] or to flatten " +
                    "response's error raising them through enclosing monad (F[ReturnType]).",
            true);
    private static final StringProperty JSONITER_VERSION = new StringProperty("jsoniterVersion",
            "The version of jsoniter-scala " +
                    "library",
            "2.31.1");

    public static final String DEFAULT_PACKAGE_NAME = "org.openapitools.client";
    private static final PackageProperty PACKAGE_PROPERTY = new PackageProperty();

    private static final List<Property<?>> properties = Arrays.asList(
            STTP_CLIENT_VERSION, USE_SEPARATE_ERROR_CHANNEL, JSONITER_VERSION, PACKAGE_PROPERTY);

    private static final Set<String> NO_JSON_CODEC_TYPES = new HashSet<>(Arrays.asList("UUID", "URI", "URL", "File", "Path"));

    private final Logger LOGGER = LoggerFactory.getLogger(ScalaSttp4JsoniterClientCodegen.class);

    protected String groupId = "org.openapitools";
    protected String artifactId = "openapi-client";
    protected String artifactVersion = "1.0.0";
    protected boolean registerNonStandardStatusCodes = true;
    protected boolean renderJavadoc = true;
    protected boolean removeOAuthSecurities = true;

    protected Map<String, String> jsonCodecNeedingTypes = new HashMap<>();

    Map<String, ModelsMap> enumRefs = new HashMap<>();

    private Map<String, String> apiNameMappings = new HashMap<>();
    private Set<String> uniqueApiNames = new HashSet<>();

    public ScalaSttp4JsoniterClientCodegen() {
        super();
        generatorMetadata = GeneratorMetadata.newBuilder(generatorMetadata)
                .stability(Stability.BETA)
                .build();

        modifyFeatureSet(features -> features
                .includeDocumentationFeatures(DocumentationFeature.Readme)
                .wireFormatFeatures(EnumSet.of(WireFormatFeature.JSON, WireFormatFeature.XML, WireFormatFeature.Custom))
                .securityFeatures(EnumSet.of(
                        SecurityFeature.BasicAuth,
                        SecurityFeature.ApiKey,
                        SecurityFeature.BearerToken))
                .excludeGlobalFeatures(
                        GlobalFeature.XMLStructureDefinitions,
                        GlobalFeature.Callbacks,
                        GlobalFeature.LinkObjects,
                        GlobalFeature.ParameterStyling)
                .excludeSchemaSupportFeatures(
                        SchemaSupportFeature.Polymorphism)
                .excludeParameterFeatures(
                        ParameterFeature.Cookie)
                .includeClientModificationFeatures(
                        ClientModificationFeature.BasePath,
                        ClientModificationFeature.UserAgent));

        outputFolder = "generated-code/scala-sttp4-jsoniter";
        modelTemplateFiles.put("model.mustache", ".scala");
        apiTemplateFiles.put("api.mustache", ".scala");
        embeddedTemplateDir = templateDir = "scala-sttp4-jsoniter";

        String jsonValueClass = "io.circe.Json";

        additionalProperties.put(CodegenConstants.GROUP_ID, groupId);
        additionalProperties.put(CodegenConstants.ARTIFACT_ID, artifactId);
        additionalProperties.put(CodegenConstants.ARTIFACT_VERSION, artifactVersion);
        additionalProperties.put("jsonCodecNeedingTypes", jsonCodecNeedingTypes.entrySet());
        if (renderJavadoc) {
            additionalProperties.put("javadocRenderer", new JavadocLambda());
        }
        additionalProperties.put("fnCapitalize", new CapitalizeLambda());
        additionalProperties.put("fnCamelize", new CamelizeLambda(false));
        additionalProperties.put("fnEnumEntry", new EnumEntryLambda());
        additionalProperties.put("fnCodecName", new CodecNameLambda());
        additionalProperties.put("fnHandleDownload", new HandleDownloadLambda());

        // importMapping.remove("Seq");
        // importMapping.remove("List");
        // importMapping.remove("Set");
        // importMapping.remove("Map");

        // TODO: there is no specific sttp mapping. All Scala Type mappings should be in
        // AbstractScala
        typeMapping = new HashMap<>();
        typeMapping.put("array", "Seq");
        typeMapping.put("set", "Set");
        typeMapping.put("boolean", "Boolean");
        typeMapping.put("string", "String");
        typeMapping.put("int", "Int");
        typeMapping.put("integer", "Int");
        typeMapping.put("long", "Long");
        typeMapping.put("float", "Float");
        typeMapping.put("byte", "Byte");
        typeMapping.put("short", "Short");
        typeMapping.put("char", "Char");
        typeMapping.put("double", "Double");
        typeMapping.put("object", jsonValueClass);
        typeMapping.put("file", "File");
        typeMapping.put("binary", "File");
        typeMapping.put("number", "Double");
        typeMapping.put("decimal", "BigDecimal");
        typeMapping.put("ByteArray", "Array[Byte]");
        typeMapping.put("AnyType", jsonValueClass);

        instantiationTypes.put("array", "ListBuffer");
        instantiationTypes.put("map", "Map");

        properties.stream()
                .map(Property::toCliOptions)
                .flatMap(Collection::stream)
                .forEach(option -> cliOptions.add(option));
    }

    @Override
    public void processOpts() {
        super.processOpts();
        properties.forEach(p -> p.updateAdditionalProperties(additionalProperties));
        invokerPackage = PACKAGE_PROPERTY.getInvokerPackage(additionalProperties);
        apiPackage = PACKAGE_PROPERTY.getApiPackage(additionalProperties);
        modelPackage = PACKAGE_PROPERTY.getModelPackage(additionalProperties);

        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));
        supportingFiles.add(new SupportingFile("build.sbt.mustache", "", "build.sbt"));
        final String invokerFolder = (sourceFolder + File.separator + invokerPackage).replace(".", File.separator);
        supportingFiles.add(new SupportingFile("jsonSupport.mustache", invokerFolder, "JsonSupport.scala"));
        supportingFiles.add(new SupportingFile("additionalTypeSerializers.mustache", invokerFolder,
                "AdditionalTypeSerializers.scala"));
        supportingFiles.add(new SupportingFile("project/build.properties.mustache", "project", "build.properties"));
    }

    @Override
    public String getName() {
        return "scala-sttp4-jsoniter";
    }

    @Override
    public String getHelp() {
        return "Generates a Scala client library (beta) based on Sttp4 and Jsoniter-Scala.";
    }

    @Override
    public String encodePath(String input) {
        String path = super.encodePath(input);
        // The parameter names in the URI must be converted to the same case as
        // the method parameter.
        StringBuffer buf = new StringBuffer(path.length());
        Matcher matcher = Pattern.compile("[{](.*?)[}]").matcher(path);
        while (matcher.find()) {
            matcher.appendReplacement(buf, "\\${" + toParamName(matcher.group(0)) + "}");
        }
        matcher.appendTail(buf);
        return buf.toString();
    }

    private PathMetadata parseAndEncodePath(String input) {
        String path = super.encodePath(input);
        ArrayList<String> pathParams = new ArrayList<>();

        // The parameter names in the URI must be converted to the same case as
        // the method parameter.
        StringBuffer buf = new StringBuffer(path.length());
        Matcher matcher = Pattern.compile("[{](.*?)[}]").matcher(path);
        while (matcher.find()) {
            matcher.appendReplacement(buf, "\\${" + toParamName(matcher.group(0)) + "}");
            pathParams.add(matcher.group(0));
        }
        matcher.appendTail(buf);
        return new PathMetadata(buf.toString(), pathParams);
    }

    @Override
    public CodegenOperation fromOperation(String path,
            String httpMethod,
            Operation operation,
            List<Server> servers) {
        CodegenOperation op = super.fromOperation(path, httpMethod, operation, servers);

        PathMetadata pathMetadata = parseAndEncodePath(path);

        op.path = pathMetadata.getPath();

        for (String pathParam : pathMetadata.getPathParams()) {
            CodegenParameter param = new CodegenParameter();
            param.isPathParam = true;
            param.baseName = pathParam;
            param.paramName = toParamName(pathParam);
            param.dataType = "String";
            param.required = true;

            boolean alreadyExists = false;
            for (CodegenParameter existingParam : op.pathParams) {
                if (existingParam.baseName.equals(param.baseName) || existingParam.paramName.equals(param.paramName)) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                op.pathParams.add(param);
                op.allParams.add(param);
            }
        }

        return op;
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    @Override
    public String escapeReservedWord(String name) {
        if (this.reservedWordsMappings().containsKey(name)) {
            return this.reservedWordsMappings().get(name);
        }
        return "`" + name + "`";
    }

    @Override
    public String toApiName(String name) {
        // first come, first served
        // if a tag name is already mapped, use that mapping
        if (apiNameMappings.containsKey(name)) {
            return apiNameMappings.get(name);
        }

        String generatedApiName = super.toApiName(name);
        String lowerCasedApiName = generatedApiName.toLowerCase(Locale.ROOT);

        // check if the name is unique (case-insensitive)
        // if it's unique, add it to the mappings and return the generated name
        if (!uniqueApiNames.contains(lowerCasedApiName)) {
            uniqueApiNames.add(lowerCasedApiName);
            apiNameMappings.put(name, generatedApiName);

            return generatedApiName;
        } else {
            // if the name is not unique, generate a new name with a unique suffix
            int i = 0;
            while (true) {
                String nextGeneratedApiName = super.toApiName(name + i);
                String lowerCasedNextGeneratedApiName = nextGeneratedApiName.toLowerCase(Locale.ROOT);
                if (!uniqueApiNames.contains(lowerCasedNextGeneratedApiName)) {
                    uniqueApiNames.add(lowerCasedNextGeneratedApiName);
                    apiNameMappings.put(name, nextGeneratedApiName);
                    
                    return nextGeneratedApiName;
                }
                i++;
            }
        }
    }

    @Override
    public ModelsMap postProcessModels(ModelsMap objs) {
        return objs;
    }

    /**
     * Invoked by {@link DefaultGenerator} after all models have been
     * post-processed,
     * allowing for a last pass of codegen-specific model cleanup.
     *
     * @param objs Current state of codegen object model.
     * @return An in-place modified state of the codegen object model.
     */
    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> objs) {
        final Map<String, ModelsMap> processed = super.postProcessAllModels(objs);
        postProcessUpdateImports(processed);
        return processed;
    }

    /**
     * Update/clean up model imports
     * <p>
     * append '._" if the import is a Enum class, otherwise
     * remove model imports to avoid warnings for importing class in the same
     * package in Scala
     *
     * @param models processed models to be further processed
     */
    @SuppressWarnings("unchecked")
    private void postProcessUpdateImports(final Map<String, ModelsMap> models) {
        final String prefix = modelPackage() + ".";

        enumRefs = getEnumRefs(models);

        for (String openAPIName : models.keySet()) {
            CodegenModel model = ModelUtils.getModelByName(openAPIName, models);
            if (model == null) {
                LOGGER.warn(
                        "Expected to retrieve model {} by name, but no model was found. Check your -Dmodels inclusions.",
                        openAPIName);
                continue;
            }

            ModelsMap objs = models.get(openAPIName);
            List<Map<String, String>> imports = objs.getImports();
            if (imports == null || imports.isEmpty()) {
                continue;
            }
            List<Map<String, String>> newImports = new ArrayList<>();
            Iterator<Map<String, String>> iterator = imports.iterator();
            while (iterator.hasNext()) {
                String importPath = iterator.next().get("import");
                Map<String, String> item = new HashMap<>();
                if (importPath.startsWith(prefix)) {
                    if (isEnumClass(importPath, enumRefs)) {
                        item.put("import", importPath.concat("._"));
                        newImports.add(item);
                    }
                } else {
                    item.put("import", importPath);
                    newImports.add(item);
                }
            }
            // reset imports
            objs.setImports(newImports);
        }
    }

    private Map<String, ModelsMap> getEnumRefs(final Map<String, ModelsMap> models) {
        Map<String, ModelsMap> enums = new HashMap<>();
        for (String key : models.keySet()) {
            CodegenModel model = ModelUtils.getModelByName(key, models);
            if (model.isEnum) {
                ModelsMap objs = models.get(key);
                enums.put(key, objs);
            }
        }
        return enums;
    }

    private boolean isEnumClass(final String importPath, final Map<String, ModelsMap> enumModels) {
        if (enumModels == null || enumModels.isEmpty()) {
            return false;
        }
        for (ModelsMap objs : enumModels.values()) {
            List<ModelMap> models = objs.getModels();
            if (models == null || models.isEmpty()) {
                continue;
            }
            for (final Map<String, Object> model : models) {
                String enumImportPath = (String) model.get("importPath");
                if (enumImportPath != null && enumImportPath.equals(importPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {
        OperationMap ops = objs.getOperations();

        for (CodegenOperation operation : ops.getOperation()) {
            if (operation.returnType != null && !NO_JSON_CODEC_TYPES.contains(operation.returnType)) {
                String identifier = formatIdentifier(operation.returnType, false) + "Codec";
                String type = operation.returnType;
                jsonCodecNeedingTypes.put(identifier, type);
            }

            if (operation.bodyParam != null && !NO_JSON_CODEC_TYPES.contains(operation.bodyParam.dataType)) {
                String identifier = formatIdentifier(operation.bodyParam.dataType, false) + "Codec";
                String type = operation.bodyParam.dataType;

                jsonCodecNeedingTypes.put(identifier, type);
            }
        }

        if (registerNonStandardStatusCodes) {
            try {
                OperationMap opsMap = objs.getOperations();
                HashSet<Integer> unknownCodes = new HashSet<>();
                for (CodegenOperation operation : opsMap.getOperation()) {
                    for (CodegenResponse response : operation.responses) {
                        if ("default".equals(response.code)) {
                            continue;
                        }
                        try {
                            int code = Integer.parseInt(response.code);
                            if (code >= 600) {
                                unknownCodes.add(code);
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.error("Status code is not an integer : response.code", e);
                        }
                    }
                }
                if (!unknownCodes.isEmpty()) {
                    additionalProperties.put("unknownStatusCodes", unknownCodes);
                }
            } catch (Exception e) {
                LOGGER.error("Unable to find operations List", e);
            }
        }

        // update imports for enum class
        List<Map<String, String>> newImports = new ArrayList<>();
        List<Map<String, String>> imports = objs.getImports();
        if (imports != null && !imports.isEmpty()) {
            Iterator<Map<String, String>> iterator = imports.iterator();
            while (iterator.hasNext()) {
                String importPath = iterator.next().get("import");
                Map<String, String> item = new HashMap<>();
                if (isEnumClass(importPath, enumRefs)) {
                    item.put("import", importPath.concat("._"));
                } else {
                    item.put("import", importPath);
                }
                newImports.add(item);
            }
        }
        objs.setImports(newImports);

        return super.postProcessOperationsWithModels(objs, allModels);
    }

    @Override
    public List<CodegenSecurity> fromSecurity(Map<String, SecurityScheme> schemes) {
        final List<CodegenSecurity> codegenSecurities = super.fromSecurity(schemes);
        if (!removeOAuthSecurities) {
            return codegenSecurities;
        }

        // Remove OAuth securities
        codegenSecurities.removeIf(security -> security.isOAuth);
        if (codegenSecurities.isEmpty()) {
            return null;
        }
        return codegenSecurities;
    }

    @Override
    public String toParamName(String name) {
        // obtain the name from parameterNameMapping directly if provided
        if (parameterNameMapping.containsKey(name)) {
            return parameterNameMapping.get(name);
        }

        return formatIdentifier(name, false);
    }

    @Override
    public String toEnumName(CodegenProperty property) {
        String identifier = formatIdentifier(property.baseName, true);

        // remove backticks because there are no capitalized reserved words in Scala
        if (identifier.startsWith("`") && identifier.endsWith("`")) {
            return identifier.substring(1, identifier.length() - 1);
        } else {
            return identifier;
        }
    }

    @Override
    public String toDefaultValue(Schema p) {
        if (p.getRequired() != null && p.getRequired().contains(p.getName())) {
            return "None";
        }

        if (ModelUtils.isBooleanSchema(p)) {
            return null;
        } else if (ModelUtils.isDateSchema(p)) {
            return null;
        } else if (ModelUtils.isDateTimeSchema(p)) {
            return null;
        } else if (ModelUtils.isNumberSchema(p)) {
            return null;
        } else if (ModelUtils.isIntegerSchema(p)) {
            return null;
        } else if (ModelUtils.isMapSchema(p)) {
            String inner = getSchemaType(ModelUtils.getAdditionalProperties(p));
            return "Map[String, " + inner + "].empty ";
        } else if (ModelUtils.isArraySchema(p)) {
            String inner = getSchemaType(ModelUtils.getSchemaItems(p));
            if (ModelUtils.isSet(p)) {
                return "Set[" + inner + "].empty ";
            }
            return "Seq[" + inner + "].empty ";
        } else if (ModelUtils.isStringSchema(p)) {
            return null;
        } else {
            return null;
        }
    }

    /**
     * Update datatypeWithEnum for array container
     *
     * @param property Codegen property
     */
    @Override
    protected void updateDataTypeWithEnumForArray(CodegenProperty property) {
        CodegenProperty baseItem = property.items;
        while (baseItem != null && (Boolean.TRUE.equals(baseItem.isMap)
                || Boolean.TRUE.equals(baseItem.isArray))) {
            baseItem = baseItem.items;
        }
        if (baseItem != null) {
            // set datetypeWithEnum as only the inner type is enum
            property.datatypeWithEnum = toEnumName(baseItem);
            // naming the enum with respect to the language enum naming convention
            // e.g. remove [], {} from array/map of enum
            property.enumName = toEnumName(property);
            property._enum = baseItem._enum;

            updateCodegenPropertyEnum(property);
        }
    }

    public static abstract class Property<T> {
        final String name;
        final String description;
        final T defaultValue;

        public Property(String name, String description, T defaultValue) {
            this.name = name;
            this.description = description;
            this.defaultValue = defaultValue;
        }

        public abstract List<CliOption> toCliOptions();

        public abstract void updateAdditionalProperties(Map<String, Object> additionalProperties);

        public abstract T getValue(Map<String, Object> additionalProperties);

        public void setValue(Map<String, Object> additionalProperties, T value) {
            additionalProperties.put(name, value);
        }
    }

    public static class StringProperty extends Property<String> {
        public StringProperty(String name, String description, String defaultValue) {
            super(name, description, defaultValue);
        }

        @Override
        public List<CliOption> toCliOptions() {
            return Collections.singletonList(CliOption.newString(name, description).defaultValue(defaultValue));
        }

        @Override
        public void updateAdditionalProperties(Map<String, Object> additionalProperties) {
            if (!additionalProperties.containsKey(name)) {
                additionalProperties.put(name, defaultValue);
            }
        }

        @Override
        public String getValue(Map<String, Object> additionalProperties) {
            return additionalProperties.getOrDefault(name, defaultValue).toString();
        }
    }

    public static class BooleanProperty extends Property<Boolean> {
        public BooleanProperty(String name, String description, Boolean defaultValue) {
            super(name, description, defaultValue);
        }

        @Override
        public List<CliOption> toCliOptions() {
            return Collections.singletonList(CliOption.newBoolean(name, description, defaultValue));
        }

        @Override
        public void updateAdditionalProperties(Map<String, Object> additionalProperties) {
            Boolean value = getValue(additionalProperties);
            additionalProperties.put(name, value);
        }

        @Override
        public Boolean getValue(Map<String, Object> additionalProperties) {
            return Boolean.valueOf(additionalProperties.getOrDefault(name, defaultValue.toString()).toString());
        }
    }

    public static class PackageProperty extends StringProperty {

        public PackageProperty() {
            super("mainPackage", "Top-level package name, which defines 'apiPackage', 'modelPackage', " +
                    "'invokerPackage'", DEFAULT_PACKAGE_NAME);
        }

        @Override
        public void updateAdditionalProperties(Map<String, Object> additionalProperties) {
            String mainPackage = getValue(additionalProperties);
            if (!additionalProperties.containsKey(CodegenConstants.API_PACKAGE)) {
                String apiPackage = mainPackage + ".api";
                additionalProperties.put(CodegenConstants.API_PACKAGE, apiPackage);
            }
            if (!additionalProperties.containsKey(CodegenConstants.MODEL_PACKAGE)) {
                String modelPackage = mainPackage + ".model";
                additionalProperties.put(CodegenConstants.MODEL_PACKAGE, modelPackage);
            }
            if (!additionalProperties.containsKey(CodegenConstants.INVOKER_PACKAGE)) {
                String invokerPackage = mainPackage + ".core";
                additionalProperties.put(CodegenConstants.INVOKER_PACKAGE, invokerPackage);
            }
        }

        public String getApiPackage(Map<String, Object> additionalProperties) {
            return additionalProperties.getOrDefault(CodegenConstants.API_PACKAGE, DEFAULT_PACKAGE_NAME + ".api")
                    .toString();
        }

        public String getModelPackage(Map<String, Object> additionalProperties) {
            return additionalProperties.getOrDefault(CodegenConstants.MODEL_PACKAGE, DEFAULT_PACKAGE_NAME + ".model")
                    .toString();
        }

        public String getInvokerPackage(Map<String, Object> additionalProperties) {
            return additionalProperties.getOrDefault(CodegenConstants.INVOKER_PACKAGE, DEFAULT_PACKAGE_NAME + ".core")
                    .toString();
        }
    }

    private static class JavadocLambda extends CustomLambda {
        @Override
        public String formatFragment(String fragment) {
            final String[] lines = fragment.split("\\r?\\n");
            final StringBuilder sb = new StringBuilder();
            sb.append("  /**\n");
            for (String line : lines) {
                sb.append("   * ").append(line).append("\n");
            }
            sb.append("   */\n");
            return sb.toString();
        }
    }

    private static class CapitalizeLambda extends CustomLambda {
        @Override
        public String formatFragment(String fragment) {
            return StringUtils.capitalize(fragment);
        }
    }

    private class EnumEntryLambda extends CustomLambda {
        @Override
        public String formatFragment(String fragment) {
            if (fragment.isBlank()) {
                return "NotPresent";
            }
            return formatIdentifier(fragment, true);
        }
    }

    private class CodecNameLambda extends CustomLambda {
        @Override
        public String formatFragment(String fragment) {
            // remove backticks because this is used as prefix for Codec generation
            return formatIdentifier(fragment, false).replace("`", "") + "Codec";
        }
    }

    private static class HandleDownloadLambda extends CustomLambda {
        @Override
        public String formatFragment(String fragment) {
            if (fragment.equals("asJson[File]")) {
                return "asFile(File.createTempFile(\"download\", \".tmp\")).mapLeft(errStr => DeserializationException(errStr, new Exception(errStr)))";
            } else {
                return fragment;
            }
        }
    }

    @Getter
    private static class PathMetadata {
        private final String path;
        private final ArrayList<String> pathParams;

        PathMetadata(String path, ArrayList<String> pathParams) {
            this.path = path;
            this.pathParams = pathParams;
        }
    }

}
