package com.example.braincheck.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.braincheck.R;
import com.example.braincheck.dao.UserDao;
import com.example.braincheck.database.AppDatabase;
import com.example.braincheck.databinding.FragmentExamsListBinding;
import com.example.braincheck.view.adapters.ExamsListAdapter;
import com.example.braincheck.viewmodel.ExamsListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExamsListFragment extends Fragment {


    private FragmentExamsListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExamsListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ExamsListViewModel viewModel = new ViewModelProvider(this).get(ExamsListViewModel.class);
            Long userId = getArguments().getLong("userId");
            // Observa as mudanças na lista de exames
            viewModel.getExamsList().observe(getViewLifecycleOwner(), exams -> {
                // Atualiza a tela com os exames recuperados
                binding.listViewExams.setAdapter(new ExamsListAdapter(requireContext(), exams));
                if (exams.isEmpty()) {
                    binding.textNoExamsMessage.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoExamsMessage.setVisibility(View.GONE);
                }
            });

            // Carrega os exames usando o ViewModel
            AppDatabase database = AppDatabase.getDatabase(requireContext());
            UserDao userDao = database.userDao();
            viewModel.loadExamsByUserId(requireContext(), userId, userDao);

        // Adiciona um OnClickListener ao botão "Novo Exame"
        FloatingActionButton buttonNewExam = binding.buttonNewExam;
        buttonNewExam.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putLong("userId", getArguments().getLong("userId"));
            args.putLong("examId", -1);
            // Navega para a tela de criação de um novo exame
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_examsList_to_examDetails, args);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
