/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pidevapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.TextField;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import static com.mycompany.ListSerie.MyApplication.theme;
import com.mycompany.pidevapp.entities.Question;
import com.mycompany.pidevapp.entities.Quiz;
import com.mycompany.pidevapp.entities.Reponse;
import com.mycompany.pidevapp.services.ServiceQuiz;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author brahm
 */
public class TakeQuiz extends BaseForm {

    Container cnt = new Container(BoxLayout.y());
    TextField tf = new TextField();
    ServiceQuiz sq = new ServiceQuiz();
    Label error = new Label();
    Quiz z;
    int score;
    HashMap<Integer, Integer> res;
    ButtonGroup btnGroup = new ButtonGroup();

    ArrayList<Integer> ids = new ArrayList();
            Button btn = new Button("Suivant");


    public TakeQuiz( int idQuiz) {
        score = 0;
        this.res = new HashMap();
        installSidemenu(theme);

        z = sq.takeQuiz(idQuiz);
        z.setId(idQuiz);

        tf.setText("0");
        tf.setVisible(false);
        error.setText("Vous devez selectioner une reponse");
        error.setVisible(false);
        fill();
        error.getAllStyles().setFgColor(16711680);

        setLayout(new BorderLayout());
        ((BorderLayout) getLayout()).setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER);
        getToolbar().setTitleComponent(
                FlowLayout.encloseCenterMiddle(
                        new Label("Passer  Quizz", "Title")
                )
        );
        Font largeBoldMonospaceFont = Font.createSystemFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);

        Label lb = createForFont(largeBoldMonospaceFont, z.getNom_quiz());
        
        Container centered = BorderLayout.centerAbsolute(lb);
        

        btn.addActionListener(event -> {
            if(btnGroup.getSelectedIndex()== -1){
                error.setVisible(true);
                revalidate();
            }else{
                error.setVisible(false);
            int nb = Integer.parseInt(tf.getText());
            Question question = z.getQuestions().get(nb);
            res.put(question.getId(), ids.get(btnGroup.getSelectedIndex()));
            if (ids.get(btnGroup.getSelectedIndex()) == question.getRep_just_id()) {
                score++;
            }

            ids = new ArrayList();
            btnGroup = new ButtonGroup();
            tf.setText(String.valueOf(nb + 1));
            fill();
            revalidate();
            }
        });

        add(BorderLayout.CENTER, cnt);

        add(BorderLayout.NORTH, centered);
        add(BorderLayout.SOUTH, btn);

    }

    public void fill() {
        Font largeBoldSystemFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        int nb = Integer.parseInt(tf.getText());
        cnt.removeAll();
        if (nb+1 == z.getQuestions().size()) {
            btn.setText("Terminer");
        }
        if (nb < z.getQuestions().size()) {
            btnGroup= new ButtonGroup();
            Question question = z.getQuestions().get(nb);
            ArrayList<Reponse> reponses = question.getReponses();
            Label l = createForFont(largeBoldSystemFont, question.getContenu_ques());
            
            
            
            cnt.add(l);
            cnt.add(error);
            error.getAllStyles().setPaddingBottom(10);
            for (int i = 0; i < reponses.size(); i++) {
                ids.add(reponses.get(i).getId());
                RadioButton rd = new RadioButton(reponses.get(i).getContenu_rep());
                Border border = Border.createCompoundBorder(Border.createLineBorder(1, 15980478), Border.createLineBorder(1, 4048823), Border.createLineBorder(1, 4048823), Border.createLineBorder(1, 4048823));
                rd.setPreferredH(150);
                rd.setPreferredW(800);
                rd.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(15980478));

                btnGroup.add(rd);
                
                cnt.add(rd);
            }
            btnGroup.addActionListener(event -> {
                error.setVisible(false);
                    for(int i = 0; i < reponses.size() ; i++){
                        if(btnGroup.getRadioButton(i).isSelected())
                            btnGroup.getRadioButton(i).getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(14789629));
                    else
                        btnGroup.getRadioButton(i).getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(15980478));
                    }
                    revalidate();
            });

        } else {
            score = score * 100 / z.getQuestions().size();
            sq = new ServiceQuiz();
            sq.addQuizResult(z.getId(), score, res);
            new ShowCandidatOffreForm().show();
            /////okhrej mel interface
        }

    }

    private Label createForFont(Font fnt, String s) {
        Label l = new Label(s);
        l.getUnselectedStyle().setFont(fnt);
        return l;
    }
}
