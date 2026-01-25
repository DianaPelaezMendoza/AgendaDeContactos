package com.example.principal.data.remote.model

data class UserDto(
    val name: NameDto,
    val location: LocationDto,
    val phone: String,
    val email: String,
    val picture: PictureDto
)
