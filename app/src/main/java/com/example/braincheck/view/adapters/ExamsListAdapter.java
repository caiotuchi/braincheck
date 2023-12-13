package com.example.braincheck.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.example.braincheck.R;
import com.example.braincheck.databinding.ExamsListItemBinding;
import com.example.braincheck.model.Exam;

import java.util.List;

public class ExamsListAdapter extends ArrayAdapter<Exam> {

    public ExamsListAdapter(Context context, List<Exam> exams) {
        super(context, 0, exams);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExamsListItemBinding binding;

        if (convertView == null) {
            // Se a view não existir, infla o layout usando DataBinding
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.exams_list_item, parent, false);

            convertView = binding.getRoot();
            convertView.setTag(new ViewHolder(binding));
        } else {
            binding = ((ViewHolder) convertView.getTag()).binding;
        }

        Exam currentExam = getItem(position);

        binding.setExam(currentExam);


        // Abrir exame
        binding.getRoot().setOnClickListener(view -> {
            // Pega o ID do exame
            long examId = currentExam.getExamId();
            Bundle args = new Bundle();
            args.putLong("examId", examId);
            Navigation.findNavController(view).navigate(R.id.action_examsList_to_examDetails, args);
        });


        // Garante que as alterações no objeto sejam feitas na hora
        binding.executePendingBindings();

        return convertView;
    }

    private static class ViewHolder {
        final ExamsListItemBinding binding;

        ViewHolder(ExamsListItemBinding binding) {
            this.binding = binding;
        }
    }
}
