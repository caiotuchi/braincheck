package com.example.braincheck.view;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.braincheck.R;
import com.example.braincheck.databinding.FragmentExamDetailsBinding;
import com.example.braincheck.model.Exam;
import com.example.braincheck.viewmodel.ExamDetailsViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.rxjava3.schedulers.Schedulers;

import android.provider.MediaStore;

public class ExamDetailsFragment extends Fragment {

    private long userId;
    private long examId;
    private ImageView imageExam;
    private TextView textFileName;
    private Button buttonTakeOrLoadImage;
    private FragmentExamDetailsBinding binding;
    private Button buttonSaveExam;
    private byte[] selectedImage;
    private ExamDetailsViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExamDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle args = getArguments();
        userId = args.getLong("userId");
        examId = args.getLong("examId", -1);

        viewModel = new ViewModelProvider(this).get(ExamDetailsViewModel.class);
        viewModel.initContext(requireContext());

        // Se houver examId, carrega os dados do exame do banco
        if (examIdExists()) {
            binding.buttonDeleteExam.setVisibility(View.VISIBLE);
            viewModel.loadExamById(examId);
            viewModel.getExamDetails().observe(getViewLifecycleOwner(), exam -> {

                // Atualizar a UI com os detalhes do exame
                binding.editName.setText(exam.getName());
                binding.editAge.setText(String.valueOf(exam.getAge()));
                binding.editGender.setText(exam.getGender());
                binding.editReport.setText(exam.getReport());

                //Lógica de exibir imagem
                if(exam.getExamImage() != null){
                    selectedImage = exam.getExamImage();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(selectedImage, 0, selectedImage.length);
                    binding.imageExam.setImageBitmap(bitmap);
                    binding.imageExam.setVisibility(View.VISIBLE);
                }
            });
        }

        binding.buttonTakeOrLoadImage.setOnClickListener(v -> {
            // Inicia a atividade para selecionar um arquivo usando o contrato de resultado
            launcher.launch("*/*");
        });


        //Salvar
        binding.buttonSaveExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exam examToCreateOrUpdate = createExamObject();
                if (examIdExists()) {
                    viewModel.updateExam(examToCreateOrUpdate);
                } else {
                    viewModel.createExam(examToCreateOrUpdate);
                }
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
            }
        });

        //deletar exame
        Button buttonDeleteExam = binding.getRoot().findViewById(R.id.buttonDeleteExam);
        buttonDeleteExam.setOnClickListener(v -> {

            viewModel.deleteExam(examId).observeOn(Schedulers.trampoline()).subscribe(() -> {
            }, throwable -> {
            });
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.popBackStack();
        });


        return view;
    }


    //exibir imagem e armazenar em variável como array de bytes
    private final ActivityResultLauncher<String> launcher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                try {
                    // Obter a Bitmap da imagem selecionada
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), result);

                    // Converter a Bitmap para um array de bytes
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    selectedImage = stream.toByteArray();

                    // Exibir a imagem selecionada no ImageView, garantindo que o ImageView não seja nulo
                    if (binding.imageExam != null) {
                        binding.imageExam.setVisibility(View.VISIBLE);
                        Picasso.get().load(result).into(binding.imageExam);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    private Exam createExamObject() {
        // Crie um objeto Exam com os dados
        Exam toSaveExam;
        String name = binding.editName.getText().toString();
        String age = binding.editAge.getText().toString();
        String gender = binding.editGender.getText().toString();
        String report = binding.editReport.getText().toString();
        if (examIdExists()) {
            toSaveExam = new Exam(this.userId, name, age, gender, report);
            toSaveExam.setExamId(this.examId);
        } else {
            toSaveExam = new Exam(this.userId, name, age, gender, report);
        }
        toSaveExam.setExamImage(selectedImage);
        return toSaveExam;
    }


    public boolean examIdExists() {
        if (examId != -1) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
