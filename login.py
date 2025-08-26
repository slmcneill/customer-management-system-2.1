from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import WebDriverException, TimeoutException
from selenium.webdriver.common.keys import Keys
import time

KEEP_BROWSER_OPEN = True
WAIT_TIME = 20  # seconds

# Helper to slow down actions
def pause(seconds=2):
    time.sleep(seconds)

try:
    driver = webdriver.Chrome()
    driver.get("http://localhost:3000/login")
    driver.implicitly_wait(10)

    # ---------- LOGIN AS ADMIN ----------
    pause(3)
    username_field = WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = driver.find_element(By.NAME, "password")
    login_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH,"//button[contains(text(),'Login')]"))
    )

    username_field.send_keys("benny")
    password_field.send_keys("pass456")

    pause(3)
    login_button.click()


    WebDriverWait(driver, WAIT_TIME).until(EC.url_contains("/app"))
    print("✅ Logged in as admin")

    # ---------- ADD CUSTOMER ----------
    name_input = WebDriverWait(driver, WAIT_TIME).until(
        EC.visibility_of_element_located((By.NAME, "name"))
    )
    email_input = driver.find_element(By.NAME, "email")
    password_input = driver.find_element(By.NAME, "password")

    pause(2)

    name_input.send_keys("Lucy Heart")
    email_input.send_keys("lucy@example.com")
    password_input.clear()
    password_input.send_keys("pass123")

    save_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//input[@type='button' and @value='Save']"))
    )
    save_button.click()
    pause(3)

    WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.XPATH, "//tr[td[contains(text(), 'Lucy Heart')]]"))
    )
    print(" Customer added: Lucy Heart")

    #Logout and Login as new customer

    current_username = "benny"
    logout_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, f"//button[contains(text(),'{current_username}')]"))
    )
    logout_button.click()
    alert = WebDriverWait(driver, WAIT_TIME).until(EC.alert_is_present())
    alert.accept()
    pause(1)
    print(" Logged out as admin")

    username_field = WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = driver.find_element(By.NAME, "password")
    login_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH,"//button[contains(text(),'Login')]"))
    )

    username_field.send_keys("lucyheart")  # login username (backend)
    pause (2)

    password_field.send_keys("pass123")

    pause(2)
    login_button.click()
    pause(1)

    WebDriverWait(driver, WAIT_TIME).until(EC.url_contains("/app"))
    print("Logged in as updated customer")



    # ---------- UPDATE CUSTOMER ----------
    customer_row = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//tr[td[contains(text(), 'Lucy Heart')]]"))
    )
    customer_row.click()
    pause(3)

    # Update display name
    name_input = WebDriverWait(driver, WAIT_TIME).until(
        EC.visibility_of_element_located((By.NAME, "name"))
    )
    email_input = driver.find_element(By.NAME, "email")
    password_input = driver.find_element(By.NAME, "password")

    pause(3)

    name_input.clear()
    name_input.send_keys("Lucy Star")

    pause(3)

    email_input.clear()
    email_input.send_keys("lucy.star@example.com")

    pause (3)

    # Clear old password before entering new password
    password_input.clear()
    password_input.send_keys("newpass999")

    save_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//input[@type='button' and @value='Save']"))
    )
    pause(3)
    save_button.click()


    WebDriverWait(driver, WAIT_TIME).until_not(
        EC.presence_of_element_located((By.XPATH, "//tr[td[contains(text(), 'Lucy Heart')]]"))
    )
    WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.XPATH, "//tr[td[contains(text(), 'Lucy Star')]]"))
    )
    print(" Customer updated: Lucy Star")

    # ---------- LOGOUT AS ADMIN ----------
    current_username = "lucyheart"
    logout_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, f"//button[contains(text(),'{current_username}')]"))
    )
    logout_button.click()
    alert = WebDriverWait(driver, WAIT_TIME).until(EC.alert_is_present())
    alert.accept()
    pause(1)
    print(" Logged out as admin")

    # ---------- LOGIN AS UPDATED CUSTOMER ----------
    username_field = WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = driver.find_element(By.NAME, "password")
    login_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH,"//button[contains(text(),'Login')]"))
    )

    username_field.send_keys("lucyheart")  # login username (backend)
    pause (2)

    password_field.send_keys("newpass999")

    pause(2)
    login_button.click()
    pause(1)

    WebDriverWait(driver, WAIT_TIME).until(EC.url_contains("/app"))
    print("Logged in as updated customer")


    # ---------- DELETE CUSTOMER ----------
    customer_row = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//tr[td[contains(text(), 'Lucy Star')]]"))
    )
    customer_row.click()
    pause(1)

    delete_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//input[@type='button' and @value='Delete']"))
    )
    pause(3)
    delete_button.click()

    WebDriverWait(driver, WAIT_TIME).until_not(
        EC.presence_of_element_located((By.XPATH, "//tr[td[contains(text(), 'Lucy Star')]]"))
    )
    print(" Customer deleted: Lucy Star")


    # ---------- CLEANUP ----------
    if KEEP_BROWSER_OPEN:
        input("Press Enter to close the browser...")

    driver.quit()
    print(" Script executed successfully!")

except (WebDriverException, TimeoutException) as e:
    print(f"Selenium Exception: {e}")
    driver.quit()