package com.azure.runtime.host.utils.templates;

import java.util.ArrayList;
import java.util.List;

/**
 * Facilitates the construction of template descriptors for processing template files. This builder pattern implementation
 * allows for a fluent and intuitive way to specify multiple templates and their output paths, streamlining the setup
 * for template processing.
 * <p>
 * The builder starts with a base template path and an optional output root path. Templates can be added with relative
 * paths, and the builder will concatenate these paths with the base paths provided at the start. This design supports
 * flexible template management, accommodating various project structures and output requirements.
 * <p>
 * Usage example:
 *
 * {@snippet lang="java" :
 * List<TemplateDescriptor> descriptors = TemplateDescriptorsBuilder.begin("/templates/", "/output/")
 *     .with("configTemplate.bicep", "configOutput.bicep")
 *     .with("resourceTemplate.bicep", "resourceOutput.bicep")
 *     .build();
 * }
 *
 * This example demonstrates setting up a builder to process templates located in the "/templates/" directory, with
 * outputs directed to the "/output/" directory. Each call to {@code with} adds a new template descriptor to the list.
 *
 * @see TemplateDescriptor for details on the template descriptor used by this builder.
 */
public class TemplateDescriptorsBuilder {
    final String templatePath;
    final String outputRootPath;

    final List<TemplateDescriptor> templateDescriptors;

    private TemplateDescriptorsBuilder(String templatePath, String outputRootPath) {
        this.templatePath = templatePath;
        this.outputRootPath = outputRootPath;
        this.templateDescriptors = new ArrayList<>();
    }

    /**
     * Begins the process of specifying template files to be processed, which will be written to the root of the
     * user-specified output directory by default.
     * @param templatePath      The path to the template files, relative to the root of the jar file / the resources
     *                          directory.
     * @return A new TemplateDescriptorsBuilder instance that can then be used to specify the template files to be
     * processed.
     */
    public static TemplateDescriptorsBuilder begin(String templatePath) {
        return begin(templatePath, "");
    }

    /**
     * Begins the process of specifying template files to be processed.
     * @param templatePath      The path to the template files, relative to the root of the jar file / the resources
     *                          directory.
     * @param outputRootPath    The root path where the output files will be written to, relative to the
     *                          user-specified output directory.
     * @return A new TemplateDescriptorsBuilder instance that can then be used to specify the template files to be
     * processed.
     */
    public static TemplateDescriptorsBuilder begin(String templatePath, String outputRootPath) {
        return new TemplateDescriptorsBuilder(templatePath, outputRootPath);
    }

    /**
     * Adds a template file to the list of descriptors with the same input and output filename, appended to the
     * previously specified base paths.
     * @param inputFilename The filename of the template file, relative to the base template path.
     * @return This builder instance to allow for method chaining.
     */
    public TemplateDescriptorsBuilder with(String inputFilename) {
        templateDescriptors.add(new TemplateDescriptor(templatePath + inputFilename, outputRootPath + inputFilename));
        return this;
    }

    /**
     * Adds a template file to the list of descriptors with specified input and output filenames, appended to the
     * previously specified base paths.
     * @param inputFilename  The filename of the template file, relative to the base template path.
     * @param outputFilename The filename for the output file, relative to the base output path.
     * @return This builder instance to allow for method chaining.
     */
    public TemplateDescriptorsBuilder with(String inputFilename, String outputFilename) {
        templateDescriptors.add(new TemplateDescriptor(templatePath + inputFilename, outputRootPath + outputFilename));
        return this;
    }

    /**
     * Completes the building process and returns the list of configured template descriptors.
     * @return A list of {@link TemplateDescriptor} instances ready for template processing.
     */
    public List<TemplateDescriptor> build() {
        return templateDescriptors;
    }
}