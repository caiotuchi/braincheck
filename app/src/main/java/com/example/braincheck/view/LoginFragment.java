package com.example.braincheck.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.braincheck.R;
import com.example.braincheck.dao.UserDao;
import com.example.braincheck.database.AppDatabase;
import com.example.braincheck.databinding.FragmentLoginBinding;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserDao userDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inicializar userDao
        userDao = AppDatabase.getDatabase(requireContext()).userDao();

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                // Verificar as credenciais usando RxJava
                checkCredentials(username, password);
            }
        });

        Button buttonGoToRegister = binding.buttonGoToRegister;
        buttonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_login_to_register);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void checkCredentials(String username, String password) {
        Single.fromCallable(() -> userDao.loginUser(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .subscribe(
                        user -> {
                            if (user != null) {
                                // Login bem-sucedido
                                Bundle args = new Bundle();
                                args.putLong("userId", user.getUserId());

                                getActivity().runOnUiThread(() -> {
                                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                                            .navigate(R.id.action_login_to_examsList, args);
                                });
                            } else {
                                // Login falhou
                                getActivity().runOnUiThread(() -> {
                                    binding.textViewError.setVisibility(View.VISIBLE);
                                });
                            }
                        },
                        throwable -> {
                            // Erro ao verificar credenciais
                            getActivity().runOnUiThread(() -> {
                                binding.textViewError.setVisibility(View.VISIBLE);
                            });
                        }
                );
    }


}
