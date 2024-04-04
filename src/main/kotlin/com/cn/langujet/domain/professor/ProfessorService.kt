package com.cn.langujet.domain.professor

import com.cn.langujet.actor.professor.payload.response.ProfessorProfileResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.user.services.AuthService
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val authService: AuthService,
    private val professorRepository: ProfessorRepository,
) {

    fun editProfile(fullName: String?, biography: String?, ieltsScore: Double?, credit: Double?): ProfessorProfileResponse {
        val professor = this.getProfessorByUserId(Auth.userId())

        if (fullName.isNullOrBlank() && biography.isNullOrBlank() && ieltsScore == null && credit == null)
            return ProfessorProfileResponse(professor)

        if (!fullName.isNullOrBlank()) professor.fullName = fullName
        if (!biography.isNullOrBlank()) professor.biography = biography
        ieltsScore?.let { professor.ieltsScore = it }
        credit?.let { professor.credit = it }

        return ProfessorProfileResponse(professorRepository.save(professor))
    }

    fun getProfessorByUserId(userId: String): Professor {
        return professorRepository.findByUser_Id(userId).orElseThrow {
            NotFoundException("Professor not found")
        }
    }
}