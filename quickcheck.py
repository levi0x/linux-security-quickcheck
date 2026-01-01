#!/usr/bin/env python3
"""
Linux Security QuickCheck
Author: Levi Davis
Description:
A quick, read-only Linux security visibility script.
Checks system info, users, network ports, SSH config, and firewall status.
"""

import os
import subprocess
from datetime import datetime


def run(cmd):
    try:
        return subprocess.check_output(cmd, shell=True, text=True).strip()
    except subprocess.CalledProcessError:
        return "Unavailable"


def header(title):
    print("\n" + "=" * 60)
    print(title)
    print("=" * 60)


def main():
    print(f"Linux Security QuickCheck")
    print(f"Run time: {datetime.now()}")
    print(f"User: {run('whoami')}")
    print(f"Hostname: {run('hostname')}")
    print(f"OS: {run('uname -a')}")

    header("Logged-in Users")
    print(run("who"))

    header("Listening Network Ports")
    print(run("ss -tuln | head -n 20"))

    header("SSH Configuration (Key Settings)")
    ssh_config = "/etc/ssh/sshd_config"
    if os.path.exists(ssh_config):
        print(run(f"grep -Ei '^(Port|PermitRootLogin|PasswordAuthentication)' {ssh_config}"))
    else:
        print("SSH config not found.")

    header("Firewall Status")
    if run("which ufw") != "Unavailable":
        print(run("ufw status"))
    else:
        print("UFW not installed.")

    print("\nQuickCheck complete.")


if __name__ == "__main__":
    main()
