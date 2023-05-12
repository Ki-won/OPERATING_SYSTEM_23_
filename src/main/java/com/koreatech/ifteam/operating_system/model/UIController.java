package com.koreatech.ifteam.operating_system.model;


import com.koreatech.ifteam.operating_system.View.OsTotalController;
import com.koreatech.ifteam.operating_system.model.packet.CorePacket;
import com.koreatech.ifteam.operating_system.model.packet.InitPacket;
import com.koreatech.ifteam.operating_system.model.packet.ProcessPacket;

public class UIController {
    private static UIController instance = new UIController(); // singleton

    private UIController(){
        //System.out.println("UIController()");
    }

    public static UIController getInstance() {// ddd
        return instance;
    }

    
    private void StateUpdateFromModel(int state) { // GUI func

    }

    public void readyProcessHandle(ProcessPacket processPacket, int IOstate) {
        
    }
    
    // Packet Send

    public void coreStatusSend() { // 현재 처리중인 정보를 GUI에 송신
        OsTotalController.coreStatusHandle(CoreManager.getInstance().getPackets());
    }

    public void resultSend(Process process) { // 처리 완료된 프로세스 정보를 GUI에 송신
        ProcessPacket tmp = process.getPacket();
        System.out.println("resultSend: "+tmp);
        OsTotalController.resultHandle(tmp);
    }

    public void readyProcessSend(Process process, int IOstate) { // ReadyQ에 들어오거나 나간 프로세스 정보를 GUI에 송신
        readyProcessHandle(process.getPacket(), IOstate); // IOState = 0, "IN" / IOState = 1, "OUT"
    }

    public void StateSend(int state) { // 상태 정보를 GUI에 송신
        // state = 0, 정상 종료 / state = 1, 비정상 종료(ex. Interrupt)
        StateUpdateFromModel(state);
    }
    
    // Packet Handle

    public void StateHandle(int state) { // 상태 정보를 GUI로부터 수신
        // state = 0, 시작 / state = 1, 인터럽트
        if (state == 0) {
            SyncManager.getInstance().run();
        } else if (state == 1) {
            SyncManager.getInstance().Interrupt();
        }
    }
    
    public void initHandle(InitPacket initPacket) { // 시작 전 초기 정보를 GUI로부터 수신
        for (int i = 0; i < initPacket.processTimes.size(); ++i) {
            Process process = new Process(Integer.toString(initPacket.processTimes.get(i).getName()),initPacket.processTimes.get(i).getAT(), initPacket.processTimes.get(i).getBT());
            ProcessManager.getInstance().addProcess(process);
        }
        CoreManager.getInstance().initCore(initPacket.coreModes);
        ScheduleManager.getInstance().setMethod(initPacket.scheduleMethod);
    }
}
