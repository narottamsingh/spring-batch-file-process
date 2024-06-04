#!/bin/bash

service_name="jamesservice"
service_script="run.sh"

start_service() {
    echo "Starting ${service_name}..."
    nohup ./${service_script} >/dev/null 2>&1 &
}

stop_service() {
    echo "Stopping ${service_name}..."
    sudo systemctl stop ${service_name}
}

status_service() {
    sudo systemctl status ${service_name}
}

check_service() {
    if ps aux | grep -v grep | grep -q "${service_script}"; then
        echo "${service_name} is running."
    else
        echo "${service_name} is not running."
    fi
}

get_pid() {
    pid=$(ps aux | grep -v grep | grep "${service_script}" | awk '{print $2}')
    if [ -z "$pid" ]; then
        echo "${service_name} is not running."
    else
        echo "${service_name} is running with PID $pid."
    fi
}

case "$1" in
    start)
        start_service
        ;;
    stop)
        stop_service
        ;;
    restart)
        stop_service
        start_service
        ;;
    status)
        status_service
        ;;
    check)
        check_service
        ;;
    pid)
        get_pid
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status|check|pid}"
        exit 1
        ;;
esac

exit 0
