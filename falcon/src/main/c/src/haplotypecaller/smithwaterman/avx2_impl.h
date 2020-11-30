#ifndef AVX2_IMPL_H
#define AVX2_IMPL_H

#include "smithwaterman_common.h"
extern int32_t (*runSWOnePairBT_fp_avx2)(int32_t match, int32_t mismatch, int32_t open, int32_t extend,uint8_t *seq1, uint8_t *seq2, int32_t len1, int32_t len2, int8_t overhangStrategy, struct Cigar* cigarRet, struct sw_ptrs ptrs);

int SWPairwiseAlignmentGKL(char* ref, int refLength, char alts[][MAX_SEQ_LEN], int batchSize, int* altLengths, struct Cigar* cigarResults, int* alignmentOffsets, int option);

int SWPairwiseAlignmentOnceGKL(char* ref, int refLength, char* alt, int altLength, int w_match, int w_mismatch, int w_open, int w_extend, int overhang_strategy, int** length_list, int** state_list, int& Cigar_list_size, int& alignment_offset);
#endif //AVX2_IMPL_H

