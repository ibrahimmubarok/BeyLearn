package com.ibeybeh.beylearn.core.base

import com.ibeybeh.beylearn.core.remote.entity.ApiResult
import com.ibeybeh.beylearn.core.remote.entity.BaseErrorResponse
import com.ibeybeh.beylearn.core.remote.entity.BaseResponse
import com.ibeybeh.beylearn.core.util.HttpErrorCode
import com.ibeybeh.beylearn.core.util.JsonUtil.fromJson
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseRemoteDataSource {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<BaseResponse<T>>
    ): ApiResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                val data = body?.data
                if (body != null && data != null) ApiResult.Success(data)
                else ApiResult.Empty()
            } else {
                val errorBody = parseErrorBody(response.errorBody())
                ApiResult.Error(errorBody)
            }
        } catch (e: SocketTimeoutException) {
            ApiResult.Error(
                BaseErrorResponse(
                    title = "Timeout Exception",
                    message = e.message,
                    httpCode = HttpErrorCode.GATEWAY_TIME_OUT
                )
            )
        } catch (e: IOException) {
            ApiResult.Error(
                BaseErrorResponse(
                    title = "IO Exception",
                    message = e.message,
                    httpCode = 999
                )
            )
        } catch (e: Exception) {
            ApiResult.Error(
                BaseErrorResponse(
                    title = "Timeout Exception",
                    message = e.message,
                    httpCode = 0
                )
            )
        }
    }

    private fun parseErrorBody(errorBody: ResponseBody?) =
        fromJson<BaseErrorResponse>(errorBody?.string().orEmpty()) ?: BaseErrorResponse(
            title = "Terjadi Kesalahan",
            message = "Terdapat kesalahan yang tidak kami ketahui",
            timestamp = "",
            httpCode = 1
        )
}