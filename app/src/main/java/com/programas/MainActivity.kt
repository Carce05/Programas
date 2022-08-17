package com.programas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.programas.databinding.ActivityMainBinding
import java.security.Principal

class MainActivity : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN=9001
    }
    private lateinit var googleSignInClient: GoogleSignInClient

    //Variable para acceder a la autenticacion
    private lateinit var auth: FirebaseAuth

    //Varaiable para acceder a los elementos de la pantalla activity.xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Se establece el enlace con la vista xml mediante el objeto binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se inicializa Firebase y se asigna el objeto para autenticación
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        binding.btRegister.setOnClickListener { haceRegistro() }
        binding.btLogin.setOnClickListener { haceLogin() }
        val gso =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient =GoogleSignIn.getClient(this,gso)
        binding.btGoogle.setOnClickListener{googleSignIn()}


    }

    private fun googleSignIn() {
        val signIntent = googleSignInClient.signInIntent
        startActivityForResult(signIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    actualiza(auth.currentUser)

                }else{
                    actualiza(null)

                }
            }
    }
    //metodo cuando regresa de hacer atenticacion en google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(cuenta.idToken!!)
            }catch (e: ApiException){

            }
        }
    }


    private fun haceLogin() {
            val email = binding.etCorreo.text.toString()
            val clave = binding.etClave.text.toString()

            //Se usa la función para crear un usuario por medio de correo y contraseña
            auth.signInWithEmailAndPassword(email,clave)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        actualiza(user)
                    } else {
                        Toast.makeText(baseContext,
                            getString(R.string.msg_fallo_login),
                            Toast.LENGTH_SHORT).show()
                        actualiza(null)
                    }
                }
        }




        private fun haceRegistro() {
            val email = binding.etCorreo.text.toString()
            val clave = binding.etClave.text.toString()

            //Se usa la función para crear un usuario por medio de correo y contraseña
            auth.createUserWithEmailAndPassword(email,clave)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        actualiza(user)
                    } else {
                        Toast.makeText(baseContext,
                            getString(R.string.msg_fallo_registro),
                            Toast.LENGTH_SHORT).show()
                        actualiza(null)
                    }
                }
        }

    private fun actualiza(user: FirebaseUser?) {
        if (user!=null) {
            // paso a la pantalla principal
            val intent = Intent(this,Principal::class.java)
            startActivity(intent)
        }
    }

    //Verifica si el usuario esta autenticado y si es asi pasa a la siguiente screen sin auth
    public override fun onStart() {
        super.onStart()
        //obtenemos el usuario actual
        val usuario = auth.currentUser
        actualiza(usuario)
    }


}