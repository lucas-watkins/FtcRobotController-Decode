package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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

        while(opModeIsActive())
        {

            boolean motorOn = gamepad1.dpad_down;
            boolean reset = gamepad1.dpad_up;

            if(motorOn) {testMotor.setPower(1.0);}
            else {testMotor.setPower(0.0);}

            if(reset)
            {
                testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if(gamepad1.dpad_left)
            {
                //  TODO find ticks per rev for our motors
                testMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                testMotor.setTargetPosition(10000);
                // make sure you don't have the call a function on the motor when it is running
                while (testMotor.getCurrentPosition() < testMotor.getTargetPosition())
                {
                    testMotor.setPower(1);
                }

                testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            postion = testMotor.getCurrentPosition();
            telemetry.addData("motor position" , postion);
            telemetry.update();


        }

    }
}
