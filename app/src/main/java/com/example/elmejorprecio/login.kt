package com.example.elmejorprecio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.elmejorprecio.R
import com.example.elmejorprecio.databinding.ActivityLoginBinding
import com.example.elmejorprecio.registro
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class login : AppCompatActivity(){
    private val GOOGLE_SING_IN =100
    var emailUsuario=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIniciar.setOnClickListener() {
            if (binding.edtEmail.text.isNotEmpty() && binding.edtPassword.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.edtEmail.text.toString(),
                    binding.edtPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, muestraProductos::class.java)
                        emailUsuario=binding.edtEmail.text.toString()
                        intent.putExtra("emailUsuario",emailUsuario)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error al autentificar el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


        //Recuperacion de contraseña
        binding.textViewOlvide.setOnClickListener(){
            val auth = FirebaseAuth.getInstance()

            val userEmail=binding.edtEmail.text.toString()
            if (binding.edtEmail.text.toString().isNotEmpty()) {
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Se ha enviado el correo electrónico de recuperación de contraseña", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(
                            this,
                            "Ha ocurrido un error al enviar el correo electrónico de recuperación de contraseña",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }else{
                Toast.makeText(this, "Rellena el email para poder mandarte el email", Toast.LENGTH_SHORT).show()
            }
        }
        //Boton Google
        binding.imgButtonGoogle.setOnClickListener(){
            val googleConf=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(
                R.string.default_web_client_id)).requestEmail().build()
            val googleClient=GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent,GOOGLE_SING_IN)
        }


        binding.btnRegistrar.setOnClickListener(){
            val intent= Intent(this, registro::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==GOOGLE_SING_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(this, muestraProductos::class.java)
                                emailUsuario= account.email.toString()
                                intent.putExtra("emailUsuario",emailUsuario)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Error al autentificar el usuario", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }catch (e:ApiException){
                Toast.makeText(this, "Error al autentificar usuario",Toast.LENGTH_SHORT).show()
            }
        }

    }
}