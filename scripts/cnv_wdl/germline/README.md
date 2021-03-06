## Running the Germline CNV WDL

### Which WDL should you use?

- Calling a cohort of samples and building a model for denoising further case samples: ``cnv_germline_cohort_workflow.wdl``
- Calling a case sample using a previously built model for denoising: ``cnv_germline_case_workflow.wdl``

#### Setting up parameter json file for a run

To get started, create the json template (using ``java -jar wdltool.jar inputs <workflow>``) for the workflow you wish to run and adjust parameters accordingly.

*Please note that there are optional workflow-level and task-level parameters that do not appear in the template file.  These are set to reasonable values by default, but can also be adjusted if desired.*

#### Required parameters in the germline cohort workflow

The reference used must be the same between PoN and case samples.

- ``CNVGermlineCohortWorkflow.cohort_entity_id`` -- Name of the cohort.  Will be used as a prefix for output filenames.
- ``CNVGermlineCohortWorkflow.contig_ploidy_priors`` -- TSV file containing prior probabilities for the ploidy of each contig, with column headers: CONTIG_NAME, PLOIDY_PRIOR_0, PLOIDY_PRIOR_1, ...
- ``CNVGermlineCohortWorkflow.gatk_docker`` -- GATK Docker image (e.g., ``broadinstitute/gatk:latest``).
- ``CNVGermlineCohortWorkflow.intervals`` -- Picard or GATK-style interval list.  For WGS, this should typically only include the chromosomes of interest.
- ``CNVGermlineCohortWorkflow.normal_bais`` -- List of BAI files.  This list must correspond to `normal_bams`.  For example, `["Sample1.bai", "Sample2.bai"]`.
- ``CNVGermlineCohortWorkflow.normal_bams`` -- List of BAM files.  This list must correspond to `normal_bais`.  For example, `["Sample1.bam", "Sample2.bam"]`.
- ``CNVGermlineCohortWorkflow.num_intervals_per_scatter`` -- Number of intervals (i.e., targets or bins) in each scatter for GermlineCNVCaller.  If total number of intervals is not divisible by the value provided, the last scatter will contain the remainder.
- ``CNVGermlineCohortWorkflow.ref_fasta_dict`` -- Path to reference dict file.
- ``CNVGermlineCohortWorkflow.ref_fasta_fai`` -- Path to reference fasta fai file.
- ``CNVGermlineCohortWorkflow.ref_fasta`` -- Path to reference fasta file.

In additional, there are optional workflow-level and task-level parameters that may be set by advanced users; for example:

- ``CNVGermlineCohortWorkflow.do_explicit_gc_correction`` -- (optional) If true, perform explicit GC-bias correction when creating PoN and in subsequent denoising of case samples.  If false, rely on PCA-based denoising to correct for GC bias.
- ``CNVGermlineCohortWorkflow.PreprocessIntervals.bin_length`` -- Size of bins (in bp) for coverage collection.  *This must be the same value used for all case samples.*
- ``CNVGermlineCohortWorkflow.PreprocessIntervals.padding`` -- Amount of padding (in bp) to add to both sides of targets for WES coverage collection.  *This must be the same value used for all case samples.*

Further explanation of other task-level parameters may be found by invoking the ``--help`` documentation available in the gatk.jar for each tool.

#### Required parameters in the germline case workflow

The reference, number of intervals per scatter, and bins (if specified) must be the same between cohort and case samples.

- ``CNVGermlineCaseWorkflow.bam`` -- Path to case BAM file.
- ``CNVGermlineCaseWorkflow.bam_idx`` -- Path to case BAM file index.
- ``CNVGermlineCaseWorkflow.contig_ploidy_model_tar`` -- Path to tar of the contig-ploidy model directory generated by the DetermineGermlineContigPloidyCohortMode task. 
- ``CNVGermlineCaseWorkflow.gatk_docker`` -- GATK Docker image (e.g., ``broadinstitute/gatk:latest``).
- ``CNVGermlineCaseWorkflow.gcnv_model_tars`` -- Array of paths to tars of the contig-ploidy model directories generated by the GermlineCNVCallerCohortMode tasks.
- ``CNVGermlineCaseWorkflow.intervals`` -- Picard or GATK-style interval list.  For WGS, this should typically only include the chromosomes of interest.
- ``CNVGermlineCaseWorkflow.num_intervals_per_scatter`` -- Number of intervals (i.e., targets or bins) in each scatter for GermlineCNVCaller.  If total number of intervals is not divisible by the value provided, the last scatter will contain the remainder.
- ``CNVGermlineCaseWorkflow.ref_fasta_dict`` -- Path to reference dict file.
- ``CNVGermlineCaseWorkflow.ref_fasta_fai`` -- Path to reference fasta fai file.
- ``CNVGermlineCaseWorkflow.ref_fasta`` -- Path to reference fasta file.

In additional, there are several task-level parameters that may be set by advanced users as above.

Further explanation of these task-level parameters may be found by invoking the ``--help`` documentation available in the gatk.jar for each tool.
