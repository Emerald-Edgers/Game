import itertools
from datetime import datetime
import os
import subprocess
import time
from pathlib import Path

import pyautogui

import logging


def run_combo(jars: tuple[str, ...], mods_dir: str, required: str):
    # Set up run command
    jar_paths = [str(mods_dir / jar) for jar in required + list(jars)]
    classpath = ":".join(jar_paths)
    cmd = ["java", "-cp", classpath, "dk.ee.zg.DesktopLauncher"]

    try:
        process = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

        time.sleep(0.25)

        simulate_key_event("enter", 0.75)

        simulate_key_event("up", 1.5)

        simulate_key_event("down", 0.5)

        simulate_key_event("space", 1)

        time.sleep(0.25)


        # Check if still running or exited
        return_code = process.poll()

        if return_code is None:
            process.terminate()  # Clean up
            return True, ""
        else:
            stderr = process.stderr.read().decode()
            return False, stderr
    except Exception as e:
        return False, str(e)


def simulate_key_event(key_code: str, delay: float) -> None:
    pyautogui.keyDown(key_code)

    time.sleep(delay)

    pyautogui.keyUp(key_code)




def test_modules(
    start_module_amount: int,
    max_combo_size: int,
    optional: list[str],
    mods_dir: Path,
    required: list[str]
) -> None:
    # Run Simulation
    print("Starting test runs")
    logging.info("Starting test runs")
    for i in range(start_module_amount, max_combo_size + 1):
        # Create a random combination from optional, with i elements.
        # The required module is not counted.
        for combo in itertools.combinations(optional, i):
            print(f"Testing combo: {combo}")
            logging.info(f"Testing combo: {combo}")
            success, error = run_combo(combo, mods_dir, required)
            if success:
                print("✅ Success\n")
                logging.info("✅ Success\n")
            else:
                print("❌ Failed with error:")
                print(error, "\n")
                logging.error("❌ Failed with error:")
                logging.error(f"Error: {error}")

def create_log_file(log_dir_path: str) -> Path:
    log_dir = Path(log_dir_path)
    log_dir.mkdir(exist_ok=True)

    timestamp = datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    log_file = log_dir / f"test_run_{timestamp}.log"

    return log_file




def main():
    # Settings
    mods_dir = Path("mods-mvn")
    required = ["GameEngine-1.0-SNAPSHOT.jar"]
    optional = [f for f in os.listdir(mods_dir) if f.endswith(".jar") and f not in required]
    


    # Configure logging
    log_file = create_log_file("mods-log")
    logging.basicConfig(
        filename=log_file,
        filemode="w",
        level=logging.INFO,
        format="%(asctime)s - %(levelname)s - %(message)s"
    )

    test_modules(len(optional) - 1, len(optional), optional, mods_dir, required)



if __name__ == "__main__":
    main()
