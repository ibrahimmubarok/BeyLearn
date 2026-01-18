package com.ibeybeh.beylearn.api_test.domain.get

import com.ibeybeh.beylearn.api_test.data.remote.model.GetProfileTestResponse
import com.ibeybeh.beylearn.api_test.data.repository.TestRepository
import com.ibeybeh.beylearn.core.base.BaseMapper
import com.ibeybeh.beylearn.core.base.BaseUseCase
import com.ibeybeh.beylearn.core.base.mapToDomain
import com.ibeybeh.beylearn.core_entity.Profile
import javax.inject.Inject

class GetProfileTestUseCase @Inject constructor(
    private val repository: TestRepository,
    private val mapper: ProfileTestMapper
) : BaseUseCase<Nothing, Profile>() {
    override fun execute(params: Nothing?) = repository.getProfile().mapToDomain { mapper.map(it) }
}

class ProfileTestMapper @Inject constructor() : BaseMapper<GetProfileTestResponse, Profile> {
    override fun map(from: GetProfileTestResponse) = Profile(
        id = from.id,
        name = from.name,
        email = from.email,
        age = from.age,
        city = from.city,
        country = from.country
    )
}