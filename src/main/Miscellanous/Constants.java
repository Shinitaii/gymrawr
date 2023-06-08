package main.Miscellanous;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Constants {
    public final static String APP_TITLE = "Gym RAWR";
    public final static ImageIcon APP_ICON = new ImageIcon("src/res/GymRAWRLogo.png");
    public final static ImageIcon HOMEPAGE_ICON = new ImageIcon("src/res/homepage.png");
    public final static ImageIcon DASHBOARD_ICON = new ImageIcon("src/res/dashboard.png");
    public final static ImageIcon USER_ICON = new ImageIcon("src/res/user.png");
    public final static ImageIcon MEMBER_ICON = new ImageIcon("src/res/member.png");
    public final static ImageIcon BLUE_MEMBER_ICON = new ImageIcon("src/res/blue_member.png");
    public final static ImageIcon TRAINER_ICON = new ImageIcon("src/res/trainer.png");
    public final static ImageIcon BLUE_TRAINER_ICON = new ImageIcon("src/res/blue_trainer.png");
    public final static ImageIcon EQUIPMENT_ICON = new ImageIcon("src/res/equipment.png");
    public final static ImageIcon BLUE_EQUIPMENT_ICON = new ImageIcon("src/res/blue_equipment.png");
    public final static ImageIcon RESERVE_EQUIPMENT_ICON = new ImageIcon("src/res/reservation.png");
    public final static ImageIcon ATTENDANCE_ICON = new ImageIcon("src/res/attendance.png");
    public final static ImageIcon BLUE_ATTENDANCE_ICON = new ImageIcon("src/res/blue_attendance.png");
    public final static ImageIcon ATTENDANCE_FORM_ICON = new ImageIcon("src/res/attendance/attendance_form.png");
    public final static ImageIcon PAYMENT_ICON = new ImageIcon("src/res/payment.png");
    public final static ImageIcon BLUE_PAYMENT_ICON = new ImageIcon("src/res/blue_payment.png");
    public final static ImageIcon SALES_REPORT_ICON = new ImageIcon("src/res/sales_report.png");
    public final static ImageIcon BLUE_SALES_REPORT_ICON = new ImageIcon("src/res/blue_sales_report.png");
    public final static ImageIcon STAFF_ICON = new ImageIcon("src/res/staff.png");
    public final static ImageIcon BLUE_STAFF_ICON = new ImageIcon("src/res/blue_staff.png");
    public final static ImageIcon POS_ICON = new ImageIcon("src/res/pos.png");
    public final static ImageIcon BLUE_POS_ICON = new ImageIcon("src/res/blue_pos.png");
    public final static ImageIcon LOGOUT_ICON = new ImageIcon("src/res/logout.png");

    public final static ImageIcon ADD_ICON = new ImageIcon("src/res/add.png");
    public final static ImageIcon UPDATE_ICON = new ImageIcon("src/res/update.png");
    public final static ImageIcon LIST_ICON = new ImageIcon("src/res/list.png");
    public final static ImageIcon BACK_ICON = new ImageIcon("src/res/back.png");
    public final static ImageIcon DONE_ICON = new ImageIcon("src/res/check.png");
    public final static ImageIcon CANCEL_ICON = new ImageIcon("src/res/cancel.png");

    public final static ImageIcon ASSIGN_TRAINER_ICON = new ImageIcon("src/res/assign_trainer.png");

    public static ImageIcon scaledImage(ImageIcon icon){
        return new ImageIcon(icon.getImage().getScaledInstance(24,24, Image.SCALE_SMOOTH));
    }

    public static ImageIcon mainScaleImage(ImageIcon icon){
        return new ImageIcon(icon.getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH));
    }

    public static ImageIcon miniScaleImage(ImageIcon icon){
        return new ImageIcon(icon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
    }
}
