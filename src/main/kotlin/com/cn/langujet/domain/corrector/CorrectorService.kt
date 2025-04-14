package com.cn.langujet.domain.corrector

import com.cn.langujet.actor.corrector.payload.response.CorrectorProfileResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.arch.advice.UnprocessableException
import org.springframework.stereotype.Service

@Service
class CorrectorService(
    private val correctorRepository: CorrectorRepository,
) {
    
    fun editProfile(
        fullName: String?,
        biography: String?,
        ieltsScore: Double?,
    ): CorrectorProfileResponse {
        val corrector = this.getCorrectorByUserId(Auth.userId())
        
        if (fullName.isNullOrBlank() && biography.isNullOrBlank() && ieltsScore == null) return CorrectorProfileResponse(
            corrector
        )
        
        if (!fullName.isNullOrBlank()) corrector.fullName = fullName
        if (!biography.isNullOrBlank()) corrector.biography = biography
        ieltsScore?.let { corrector.ieltsScore = it }
        
        return CorrectorProfileResponse(correctorRepository.save(corrector))
    }
    
    fun getCorrectorByUserId(userId: String): CorrectorEntity {
        return correctorRepository.findByUser_Id(userId).orElseThrow {
            UnprocessableException("Corrector not found")
        }
    }
    
    fun correctorExistsByUserId(userId: String): Boolean {
        return correctorRepository.existsByUser_Id(userId)
    }
}