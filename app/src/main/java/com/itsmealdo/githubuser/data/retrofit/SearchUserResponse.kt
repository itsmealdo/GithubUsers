package com.itsmealdo.githubuser.data.retrofit

import com.google.gson.annotations.SerializedName
import com.itsmealdo.githubuser.data.model.UserDetailResponse

data class SearchUserResponse(
    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("items")
    val users: List<UserDetailResponse>
)
