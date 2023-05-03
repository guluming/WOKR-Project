package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.Editor.UserEditor;
import com.slamdunk.WORK.dto.request.UserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String team;
//    @Column(nullable = false)
//    private String teamPosition;
    @Column
    private boolean firstLogin = true;
    @Column
    private boolean secessionState;

    public User(UserRequest params) {
        this.email = params.getEmail();
        this.password = params.getPassword();
        this.name = params.getName();
        this.team = params.getTeam();
//        this.teamPosition = params.getTeamposition();
    }

    public void UserEdit(UserEditor userEditor) {
        name = userEditor.getName();
        team = userEditor.getTeam();
//        teamPosition = userEditor.getTeamPosition();
        firstLogin = userEditor.isFirstLogin();
        secessionState = userEditor.isSecessionState();
    }

    public UserEditor.UserEditorBuilder UserToEditor() {
        return UserEditor
                .builder()
                .name(name)
                .team(team)
//                .teamPosition(teamPosition)
                .firstLogin(firstLogin)
                .secessionState(secessionState);
    }

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
