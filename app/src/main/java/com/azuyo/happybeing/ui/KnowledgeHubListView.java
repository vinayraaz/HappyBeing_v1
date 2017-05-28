package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Views.AudioFilesInfo;
import com.azuyo.happybeing.Views.CustomKnowledgeHubAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 24-03-2017.
 */
public class KnowledgeHubListView extends BaseActivity implements OnItemSelectedListener {
    private ListView knowledge_hub_list;
    private CustomKnowledgeHubAdapter customKnowledgeHubAdapter;
    private List<AudioFilesInfo> infoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knowldege_hub);
        setListToView();
        knowledge_hub_list = (ListView) findViewById(R.id.knowledge_hub_list);
        customKnowledgeHubAdapter = new CustomKnowledgeHubAdapter(this, infoList, this);
        knowledge_hub_list.setAdapter(customKnowledgeHubAdapter);
    }

    private void setListToView() {
        infoList = new ArrayList<>();
        AudioFilesInfo audioFilesInfo = new AudioFilesInfo(0, "Factors that impact performance");
        AudioFilesInfo audioFilesInfo1 = new AudioFilesInfo(1, "What are negative core beliefs and its influence on your goal attainment");
        AudioFilesInfo audioFilesInfo2 = new AudioFilesInfo(2, "What stops you from achieving your goals");
        AudioFilesInfo audioFilesInfo3 = new AudioFilesInfo(3, "How do you utilise your time to get best results");
        AudioFilesInfo audioFilesInfo4 = new AudioFilesInfo(4, "Effective note taking skills for quick learning");
        AudioFilesInfo audioFilesInfo5 = new AudioFilesInfo(5, "Effective memory management skills");
        AudioFilesInfo audioFilesInfo6 = new AudioFilesInfo(6, "Effective test taking skills for better outcomes");
        AudioFilesInfo audioFilesInfo7 = new AudioFilesInfo(7, "Test preparation and on the day of exam");
        AudioFilesInfo audioFilesInfo8 = new AudioFilesInfo(8, "How to answer short and essay questions");

        infoList.add(audioFilesInfo);
        infoList.add(audioFilesInfo1);
        infoList.add(audioFilesInfo2);
        infoList.add(audioFilesInfo3);
        infoList.add(audioFilesInfo4);
        infoList.add(audioFilesInfo5);
        infoList.add(audioFilesInfo6);
        infoList.add(audioFilesInfo7);
        infoList.add(audioFilesInfo8);
    }

    @Override
    public void onItemSelected(AudioFilesInfo audioFilesInfo) {
        int id = audioFilesInfo.getId();
        Intent intent = new Intent(this, KnowledgeHubDayLayout.class);
        switch (id) {
            case 0:
                intent.putExtra("DAY_NUMBER", 0);
                break;
            case 1:
                intent.putExtra("DAY_NUMBER", 1);
                break;
            case 2:
                intent.putExtra("DAY_NUMBER", 2);
                break;
            case 3:
                intent.putExtra("DAY_NUMBER", 3);
                break;
            case 4:
                intent.putExtra("DAY_NUMBER", 4);
                break;
            case 5:
                intent.putExtra("DAY_NUMBER", 5);
                break;
            case 6:
                intent.putExtra("DAY_NUMBER", 6);
                break;
            case 7:
                intent.putExtra("DAY_NUMBER", 7);
                break;
            case 8:
                intent.putExtra("DAY_NUMBER", 8);
                break;

        }
        startActivity(intent);
    }

    @Override
    public void onDownloadButtonClicked(AudioFilesInfo audioFilesInfo) {

    }
}
