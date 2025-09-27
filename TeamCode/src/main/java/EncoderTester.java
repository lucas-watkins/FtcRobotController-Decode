

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name="encoderTest", group="Linear OpMode")
public class EncoderTester extends LinearOpMode
{
    private DcMotor testMotor = null;




    public void runOpMode() {
        testMotor = hardwareMap.get(DcMotor.class, "test");
        testMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();


        int postion;
        String output;
        while(opModeIsActive())
        {
            testMotor.setPower(1.0);
            postion = testMotor.getCurrentPosition();
            telemetry.addData("motor position" , postion);
        }



    }
}
