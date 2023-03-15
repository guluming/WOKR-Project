package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.Editor.ObjectiveEditor;
import com.slamdunk.WORK.dto.request.ObjectiveRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Objective extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objective_id")
    private Long id;
    @Column(nullable = false)
    private String objective;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private String color;
    @Column
    private int progress;
    @Column
    private boolean deleteState;

    public Objective(ObjectiveRequest param) {
        this.objective = param.getObjective();
        this.startDate = param.getStartdate();
        this.endDate = param.getEnddate();
        this.color = param.getColor();
        this.progress = 0;
    }

    public void ObjectiveEdit(ObjectiveEditor objectiveEditor) {
        objective = objectiveEditor.getObjective();
        startDate = objectiveEditor.getStartdate();
        endDate = objectiveEditor.getEnddate();
        color = objectiveEditor.getColor();
        progress = objectiveEditor.getProgress();
    }

    public ObjectiveEditor.ObjectiveEditorBuilder ObjectiveToEditor() {
        return ObjectiveEditor
                .builder()
                .objective(objective)
                .startdate(startDate)
                .enddate(endDate)
                .color(color)
                .progress(progress);
    }
}
