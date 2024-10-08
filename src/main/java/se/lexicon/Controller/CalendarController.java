package se.lexicon.Controller;

import se.lexicon.Dao.CalendarDao;
import se.lexicon.Dao.MeetingDao;
import se.lexicon.Dao.UserDao;
import se.lexicon.exception.CalendarExceptionHandler;
import se.lexicon.model.Calendar;
import se.lexicon.model.User;
import se.lexicon.view.CalendarView;

public class CalendarController {

    //dependencies:
    private CalendarView view;
    private UserDao userDao;
    private CalendarDao calendarDao;
    private MeetingDao meetingDao;

    //fields:
    private boolean isLoggedIn;
    private String username;

    public CalendarController(CalendarView view, UserDao userDAO, CalendarDao calendarDAO, MeetingDao meetingDao) {
        this.view = view;
        this.userDao = userDAO;
        this.calendarDao = calendarDAO;
        this.meetingDao = meetingDao;
    }

    public void run() {
        while (true) {
            view.displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 0:
                    register();
                    break;
                case 1:
                    login();
                    break;
                case 2:
                    createCalendar();
                    break;
                case 3:
                    //todo call create meeting method
                    break;
                case 4:
                    //todo call delete calendar method
                    break;
                case 5:
                    //todo call display calendar method
                    break;
                case 6:
                    isLoggedIn = false;
                    view.displayMessage("You are logged out.");
                    break;
                case 7:
                    //todo call exit
                    System.exit(0);
                    break;
                default:
                    view.displayWarningMessage("Invalid choice. Please select a valid option.");
            }
        }
    }

    private int getUserChoice() {
        String operationType = view.promoteString();
        int choice = -1;
        try {
            choice = Integer.parseInt(operationType);
        } catch (NumberFormatException e) {
            view.displayErrorMessage("Invalid input, please enter a number.");
        }
        return choice;
    }

    private void register() {
        view.displayMessage("Enter your username");
        String username = view.promoteString();
        User registeredUser = userDao.createUser(username);
        view.displayUser(registeredUser);
    }

    private void login() {
        User user = view.promoteUserForm();
        try {
            isLoggedIn = userDao.authenticate(user);
            username = user.getUsername();
            view.displaySuccessMessage("Login successful. Welcome " + username);
        } catch (Exception e) {
            CalendarExceptionHandler.handleException(e);
        }
    }

    private void createCalendar() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        String calendarTitle = view.promoteCalendarForm();
        Calendar createdCalendar = calendarDao.createCalendar(calendarTitle, username);
        view.displaySuccessMessage("Calendar created successfully.");
        view.displayCalendar(createdCalendar);

    }
}
