package com.example.braincheck;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.braincheck.view.RegisterFragment;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class AccountValidationUnitTest {
    @Test
    public void accountValidationTest() {
        assertFalse(RegisterFragment.checkValidAccount("  a", "as "));
    }
}


//update funcionando ok
//Upload funcionando ok
//Visualização de imagem ok
//teste instrumentado de cadastro em que o toast de inválido é exibido e um que não é exibido