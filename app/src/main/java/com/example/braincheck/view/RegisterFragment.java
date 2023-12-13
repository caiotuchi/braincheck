package com.example.braincheck.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.braincheck.R;
import com.example.braincheck.dao.UserDao;
import com.example.braincheck.database.AppDatabase;
import com.example.braincheck.databinding.FragmentRegisterBinding;
import com.example.braincheck.model.User;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private UserDao userDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userDao = AppDatabase.getDatabase(requireContext()).userDao();

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.editRegisterUsername.getText().toString();
                String password = binding.editRegisterPassword.getText().toString();
                if (checkValidAccount(username,password)){
                    checkUsernameAvailability(username, password);
                } else {
                    Toast.makeText(requireContext(), "Usuário e senha devem ter mais de 3 caracteres.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    private void checkUsernameAvailability(String username, String password) {
        userDao.getUserByUsername(username).observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                // Usuário disponível
                binding.textErrorMessage.setVisibility(View.INVISIBLE);
                registerUser(username, password);
            } else {
                // Usuário já existe
                binding.textErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void registerUser(String username, String password) {
        // Insere o usuário no banco de dados e obtém o ID do usuário
        Single<Long> insertUserSingle = Single.fromCallable(() -> userDao.insertUser(new User(username, password)))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline());

        // Inscreve-se no Single para lidar com sucesso ou erro
        insertUserSingle.subscribe(
                userId -> {
                    // Registro bem-sucedido
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Registro bem-sucedido! Bem vindo!", Toast.LENGTH_SHORT).show();

                        // Cria um Bundle para passar dados para a próxima tela
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putLong("userId", userId);
                        // Manda para a tela de exames com o Bundle
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.action_register_to_examsList, bundle);
                    });
                },
                throwable -> {
                    // Erro ao registrar usuário
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Erro ao registrar usuário: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    });
                });
    }


    public static boolean checkValidAccount(String username, String password){
        if (username.length() > 3
                && !username.contains(" ")
                && password.length() > 3
                && !password.contains(" ")){
            return true;
        } else {
            return false;
        }
    }
}