package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.task.dto.TaskOptionDTO;
import excepion.CourseStatusException;
import excepion.TaskValidationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskValidatorService {

    public void validateStatement(String statement) {
        if (statement == null || statement.trim().length() < 4 || statement.trim().length() > 255) {
            throw new TaskValidationException("O enunciado deve ter entre 4 e 255 caracteres.");
        }
    }

    public void validateCourseStatus(Course course) {
        if (course.getStatus() == Status.BUILDING) {
            throw new CourseStatusException("Não é possível adicionar uma atividade a um curso que não está em BUILDING.");
        }
    }

    public void validateOrder(int order, int maxOrderAllowed) {
        if (order <= 0) {
            throw new TaskValidationException("A ordem deve ser um número inteiro positivo.");
        }
        if (order > maxOrderAllowed + 1) {
            throw new TaskValidationException("A sequência de ordem está incorreta.");
        }
    }

    public void validateOptions(List<TaskOptionDTO> options, String statement, Type type) {
        if (type == Type.OPEN_TEXT) return;

        if (options == null || options.isEmpty()) {
            throw new TaskValidationException("As opções não podem estar vazias.");
        }

        Set<String> uniqueOptions = new HashSet<>();
        int correctCount = 0;

        for (TaskOptionDTO option : options) {
            String text = option.getOption().trim();
            if (text.length() < 4 || text.length() > 80) {
                throw new TaskValidationException("Cada opção deve ter entre 4 e 80 caracteres.");
            }
            if (text.equals(statement)) {
                throw new TaskValidationException("A opção não pode ser igual ao enunciado da atividade.");
            }
            if (!uniqueOptions.add(text)) {
                throw new TaskValidationException("Não são permitidas opções duplicadas.");
            }
            if (option.isCorrect()) {
                correctCount++;
            }
        }

        if (type == Type.SINGLE_CHOICE) {
            if (options.size() < 2 || options.size() > 5) {
                throw new TaskValidationException("Atividade de alternativa única deve ter entre 2 e 5 opções.");
            }
            if (correctCount != 1) {
                throw new TaskValidationException("Atividade de alternativa única deve ter exatamente 1 opção correta.");
            }
        }

        if (type == Type.MULTIPLE_CHOICE) {
            if (options.size() < 3 || options.size() > 5) {
                throw new TaskValidationException("Atividade de múltipla escolha deve ter entre 3 e 5 opções.");
            }
            if (correctCount < 2 || correctCount == options.size()) {
                throw new TaskValidationException("Atividade de múltipla escolha deve ter pelo menos 2 opções corretas e pelo menos 1 incorreta.");
            }
        }
    }
}
