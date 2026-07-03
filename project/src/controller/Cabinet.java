package controller;

import data.DataManager;

/**
 * Master controller class that holds and coordinates all manager instances.
 * Modeled after the Cabinet pattern often used in course templates.
 */
public class Cabinet {
    private PlayerManager playerManager;
    private TrainingManager trainingManager;
    private MatchManager matchManager;
    private SalaryManager salaryManager;
    private ReportManager reportManager;
    private DataManager dataManager;

    public Cabinet() {
        this.playerManager = new PlayerManager();
        this.trainingManager = new TrainingManager();
        this.matchManager = new MatchManager();
        this.salaryManager = new SalaryManager();
        this.reportManager = new ReportManager();
        this.dataManager = new DataManager(this);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public TrainingManager getTrainingManager() {
        return trainingManager;
    }

    public MatchManager getMatchManager() {
        return matchManager;
    }

    public SalaryManager getSalaryManager() {
        return salaryManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
