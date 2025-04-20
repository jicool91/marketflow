#!/bin/bash

echo "[INFO] Adding GitHub to known_hosts..."
mkdir -p /var/jenkins_home/.ssh
ssh-keyscan github.com >> /var/jenkins_home/.ssh/known_hosts
chown -R jenkins:jenkins /var/jenkins_home/.ssh
chmod 600 /var/jenkins_home/.ssh/known_hosts
