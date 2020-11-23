package utilPackage

sealed  class ResourceUsed<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T): ResourceUsed<T>(data)
    class Error<T>(message: String, data: T? = null) : ResourceUsed<T>(data,message)
    class Loading<T>: ResourceUsed<T>()
}