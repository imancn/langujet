package com.cn.langujet.actor.correction.api

import com.cn.langujet.actor.correction.payload.response.CorrectionResponse
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionRequest
import com.cn.langujet.actor.correction.payload.response.CorrectorAvailableCorrectionResponse
import com.cn.langujet.actor.correction.payload.response.CorrectorCorrectionExamSessionContentResponse
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.correction.service.CorrectionService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/ai")
@Validated
class AICorrectionController(
    private val correctionService: CorrectionService
) {
    @GetMapping("/corrections/pending")
    @PreAuthorize("hasRole('CORRECTOR_AI')")
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        return correctionService.getCorrectorPendingCorrections(CorrectorType.AI)
    }
    
    @PostMapping("/corrections/assign")
    @PreAuthorize("hasRole('CORRECTOR_AI')")
    fun assignCorrection(
        @RequestBody assignCorrectionRequest: AssignCorrectionRequest
    ): CorrectionResponse {
        return correctionService.assignCorrectionByCorrector(assignCorrectionRequest, CorrectorType.AI)
    }
    
    @GetMapping("/corrections/processing")
    @PreAuthorize("hasRole('CORRECTOR_AI')")
    fun getCorrectorProcessingCorrection(): CorrectionResponse {
        return correctionService.getCorrectorProcessingCorrection()
    }
    
    @GetMapping("/corrections")
    @PreAuthorize("hasRole('CORRECTOR_AI')")
    fun getCorrectorCorrectionsByStatus(@RequestParam status: CorrectionStatus): List<CorrectionResponse> {
        return correctionService.getCorrectorCorrectionsByStatus(status)
    }
    
    @GetMapping("/corrections/exam-session-contents/{correctionId}")
    @PreAuthorize("hasRole('CORRECTOR_AI')")
    fun getCorrectorCorrectionExamSessionContent(
        @PathVariable correctionId: String
    ): CorrectorCorrectionExamSessionContentResponse {
        return correctionService.getCorrectorCorrectionExamSessionContent(correctionId)
    }
}