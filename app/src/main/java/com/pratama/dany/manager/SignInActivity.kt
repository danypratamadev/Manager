package com.pratama.dany.manager

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.DashResult
import com.pratama.dany.manager.api.model.LoginResult
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mdialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setSupportActionBar(findViewById(R.id.theToolBar))
        supportActionBar?.title = "Kuah Panas"

        sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
        masuk?.setOnClickListener(this)

        email?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                val currentEmail = p0.toString()

                if(!TextUtils.isEmpty(currentEmail)){

                    if(isEmailValid(currentEmail) == false){
                        tiEmail.isErrorEnabled = true
                        tiEmail.error = "Email tidak valid! Periksa kembali email anda!"
                    } else {
                        tiEmail.isErrorEnabled = false
                    }

                } else {

                    tiEmail?.isErrorEnabled = true
                    tiEmail?.error = "Masukkan email!"

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            fun isEmailValid(email: CharSequence?): Boolean {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            }

        })

        password?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                val currentPassword = p0.toString()

                if(!TextUtils.isEmpty(currentPassword)){

                    tiPassword?.isErrorEnabled = false

                } else {

                    tiPassword?.isErrorEnabled = true
                    tiPassword?.error = "Masukkan password!"

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

    }

    override fun onClick(p0: View?) {

        when (p0?.id) {

            R.id.masuk -> {

                email.clearFocus()
                password.clearFocus()

                val emailTxt = email?.text.toString()
                val passwordTxt = password?.text.toString()

                if (!TextUtils.isEmpty(emailTxt) && !TextUtils.isEmpty(passwordTxt)) {

                    showProgressDialog()
                    signToApps(emailTxt, passwordTxt)

                } else {

                    if (TextUtils.isEmpty(emailTxt)) {
                        tiEmail?.isErrorEnabled = true
                        tiEmail?.error = "Masukkan email!"
                    }

                    if (TextUtils.isEmpty(passwordTxt)) {
                        tiPassword?.isErrorEnabled = true
                        tiPassword?.error = "Masukkan password!"
                    }

                }

            }

        }

    }

    private fun signToApps(emailTxt: String, passwordTxt: String){

        val masuk = ApiEndPoint.REGISTER_API.loginApps(emailTxt, passwordTxt, 2, ApiEndPoint.KEY_ACCESS)
        masuk.enqueue(object : Callback<LoginResult> {

            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val message = response.body()?.message
                    val iduser = response.body()?.result?.iduser
                    val nama = response.body()?.result?.nama
                    val email = response.body()?.result?.email
                    val role = response.body()?.result?.role
                    val cabang = response.body()?.result?.cabang
                    val namacabang = response.body()?.result?.namacabang
                    val alamatcabang = response.body()?.result?.alamatcabang

                    if(status!!){

                        mdialog.dismiss()
                        getDashToday(cabang!!)
                        stratSession(iduser!!, nama!!, email!!, role!!, cabang!!, namacabang!!, alamatcabang!!)
                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        Handler().postDelayed({

                            mdialog.dismiss()
                            if(message.equals("Email tidak terdaftar!")){

                                tiEmail.isErrorEnabled = true
                                tiEmail.error = message
                                tiEmail.requestFocus()

                            } else {

                                tiPassword.isErrorEnabled = true
                                tiPassword.error = message
                                tiPassword.requestFocus()

                            }

                        }, 1500)

                    }

                } else {

                    Handler().postDelayed({

                        mdialog.dismiss()
                        Toast.makeText(this@SignInActivity, call.toString(), Toast.LENGTH_SHORT).show()

                    }, 1500)

                }

            }

            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                Handler().postDelayed({

                    mdialog.dismiss()
                    Toast.makeText(this@SignInActivity, "Periksa Kembali Jaringan Internet!", Toast.LENGTH_LONG).show()

                }, 1500)
                Log.e("Error Rest Api:", t.toString())
            }

        })

    }

    private fun getDashToday(idCabang: Int){

        val dash = ApiEndPoint.REGISTER_API.getDashToday("sfYZONlALBQsBMU6dGzlJVWG17M6qx27", idCabang)
        dash.enqueue(object: Callback<DashResult> {

            override fun onResponse(call: Call<DashResult>, response: Response<DashResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val message = response.body()?.message
                    val transakasi = response.body()?.result?.transaksi
                    val item = response.body()?.result?.item
                    val total = response.body()?.result?.total

                    if(status!!){

                        saveDashValue(transakasi!!, item!!, total!!)

                    } else {

                        Toast.makeText(this@SignInActivity, message, Toast.LENGTH_SHORT).show()

                    }

                } else {

                    Toast.makeText(this@SignInActivity, "Gagal!!", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<DashResult>, t: Throwable) {
                Toast.makeText(this@SignInActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                Log.e("Error Rest Api:", t.toString())
            }

        })

    }

    private fun saveDashValue(transaksi: Int, item: Int, total: Int){

        sharedPreferences = getSharedPreferences("DASH", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("transaksi", transaksi)
        editor.putInt("item", item)
        editor.putInt("total", total)
        editor.apply()

    }

    private fun stratSession(iduser: String, nama: String, email: String, role: Int, cabang: Int,
                             namacabang: String, alamatcabang: String){

        val editor = sharedPreferences.edit()
        editor.putBoolean("LOG", true)
        editor.putString("iduser", iduser)
        editor.putString("nama", nama)
        editor.putString("email", email)
        editor.putInt("role", role)
        editor.putInt("cabang", cabang)
        editor.putString("namacabang", namacabang)
        editor.putString("alamatcabang", alamatcabang)
        editor.apply()

    }

    private fun showProgressDialog(){

        mdialog = Dialog(this)
        mdialog.setCancelable(false)
        mdialog.setContentView(R.layout.progress_dialog)

        val caption = mdialog.findViewById<TextView>(R.id.caption)
        caption.text = "Memasukkan anda..."

        mdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mdialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        mdialog.show()

    }

}
