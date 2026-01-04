package com.example.checkexclusivecontrol

class User(id: Int, name: String, email: String) {

    private var id: Int = id
    internal var name: String = name
    internal var email: String = email

    // idを取得するメソッド
    fun getId(): Int {
        return id
    }

    // nameを取得するメソッド
    fun getName(): String {
        return name
    }

    // emailを取得するメソッド
    fun getEmail(): String {
        return email
    }

    // idを更新するメソッド
    fun setId(id: Int) {
        this.id = id
    }

    // setName(), setEmail() も同様に書ける
}
