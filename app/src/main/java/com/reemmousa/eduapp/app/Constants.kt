package com.reemmousa.eduapp.app

object Constants {
    const val BASE_URL = "https://www.udemy.com/api-2.0/"
    private const val UDEMY_API_KEY = "Basic eTNGZHJMSEhDaUFjR3JHVEkwRlJTY2ZNNTl1eEMzNF" +
            "BCY0taOTRDVzo0cHUzT3REWXBCR2RROVJBWlhnNnRLMTE4WGRMcWlPSkx5Y2JNSFhFd2l4" +
            "R1VieXV2cGRWTUp4RW1rN0VBdkxqdWQ3Y1dKaGJXelNZdTZYZmlBbW9JQmhxODFaanhNS1k3R0" +
            "V2djRuS3htZGk1bWhoZ2Z3YWdsZHZSREx0bXpxcw=="

    const val HEADER_ACCESS = "Accept: application/json, text/plain, */*"
    const val HEADER_CONTENT_TYPE = "Content-Type: application/json;charset=utf-8"
    const val HEADER_AUTHORIZATION = "Authorization: $UDEMY_API_KEY"

    const val UDEMY_BASE_URL = "https://www.udemy.com"

    // SEARCH QUERIES
    const val SEARCH_ALL = "all"
    const val SEARCH_DEVELOPMENT = "Development"
    const val SEARCH_BUSINESS = "Business"
    const val SEARCH_MARKETING = "Marketing"
    const val SEARCH_GRAPHIC_DESIGN = "Graphic Design"
    const val SEARCH_IT_AND_SECURITY = "IT & Security"
    const val SEARCH_ACCOUNTING = "Accounting"
    const val QUERY_PRICE_FREE = "price-free"
    const val QUERY_PRICE_PAID = "price-paid"

    // Prefs
    const val COURSE_PRICE_FREE = "Free"
    const val COURSE_PRICE_PAID = "Paid"
    const val DEFAULT_COURSE_CATEGORY = "Business"
    const val PREFS = "prefs"
    const val PREF_PRICE = "prefPrice"
    const val PREF_PRICE_ID = "prefPriceId"
    const val PREF_CATEGORY = "prefCategory"
    const val PREF_CATEGORY_ID = "prefCategoryId"

    // Room DB
    const val DATABASE_NAME = "courses_database"
    const val COURSES_TABLE = "courses_table"
    const val WISHLIST_TABLE = "wishlist_table"
    const val CART_TABLE = "cart_table"

    const val PRIVACY_POLICY_URL = "https://app.termly.io/dashboard/website/4e330de6-6129-416a-bebc-afc50782465d/privacy-policy#infosafe"
}