package org.broadinstitute.hellbender.utils.help;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import org.broadinstitute.barclay.help.DocumentedFeature;
import org.broadinstitute.barclay.help.DocWorkUnit;
import org.broadinstitute.barclay.help.GSONWorkUnit;
import org.broadinstitute.barclay.help.HelpDoclet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Custom Barclay-based Javadoc Doclet used for generating GATK help/documentation.
 *
 * NOTE: Methods in this class are intended to be called by Gradle/Javadoc only, and should not be called
 * by methods that are used by the GATK runtime. This class has a dependency on com.sun.javadoc classes,
 * which may not be present since they're not provided as part of the normal GATK runtime classpath.
 */
public class GATKHelpDoclet extends HelpDoclet {

    private final static String GATK_FREEMARKER_INDEX_TEMPLATE_NAME = "generic.index.template.html";
    private final static String WALKER_TYPE_MAP_ENTRY = "walkertype";      // populated from javadoc custom tag

    /**
     * Create a doclet of the appropriate type and generate the FreeMarker templates properties.
     * @param rootDoc
     * @throws IOException
     */
    public static boolean start(final RootDoc rootDoc) throws IOException {
        return new GATKHelpDoclet().startProcessDocs(rootDoc);
    }

    /**
     * Return the name of the freemarker template to be used for the index generated by Barclay.
     * Must reside in the folder passed to the Barclay Javadc Doclet via the "-settings-dir" parameter.
     * @return name of freemarker index template
     */
    @Override
    public String getIndexTemplateName() {
        return GATK_FREEMARKER_INDEX_TEMPLATE_NAME;
    }

    /**
     * @return Create and return a DocWorkUnit-derived object to handle documentation
     * for the target feature(s) represented by documentedFeature.
     *
     * @param documentedFeature DocumentedFeature annotation for the target feature
     * @param classDoc javadoc classDoc for the target feature
     * @param clazz class of the target feature
     * @return DocWorkUnit to be used for this feature
     */
    @Override
    protected DocWorkUnit createWorkUnit(
            final DocumentedFeature documentedFeature,
            final ClassDoc classDoc,
            final Class<?> clazz)
    {
        return new GATKDocWorkUnit(
                new GATKHelpDocWorkUnitHandler(this),
                documentedFeature,
                classDoc,
                clazz);
    }

    /**
     * Create a GSONWorkUnit-derived object that holds our custom data. This method should create the object, and
     * propagate any custom javadoc tags from the template map to the newly created GSON object; specifically
     * "walkertype", which is pulled from a custom javadoc tag.
     *
     * @param workUnit work unit for which a GSON object is required
     * @param groupMaps
     * @param featureMaps
     * @return a GSONWorkUnit-derived object for this work unit, populated with any custom values
     */
    @Override
    protected GSONWorkUnit createGSONWorkUnit(
            final DocWorkUnit workUnit,
            final List<Map<String, String>> groupMaps,
            final List<Map<String, String>> featureMaps)
    {
        GATKGSONWorkUnit gatkGSONWorkUnit = new GATKGSONWorkUnit();
        gatkGSONWorkUnit.setWalkerType((String)workUnit.getRootMap().get(WALKER_TYPE_MAP_ENTRY));
        return gatkGSONWorkUnit;
    }

    /**
     * Adds a super-category so that we can custom-order the categories in the doc index
     *
     * @param docWorkUnit
     * @return
     */
    @Override
    protected final Map<String, String> getGroupMap(final DocWorkUnit docWorkUnit) {
        final Map<String, String> root = super.getGroupMap(docWorkUnit);

        /**
         * Add-on super-category definitions. The super-category and spark value strings need to be the
         * same as used in the Freemarker template.
         */
        root.put("supercat", HelpConstants.getSuperCategoryProperty(docWorkUnit.getGroupName()));

        return root;
    }

}
