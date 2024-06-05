#!/bin/bash

service_name="james"
service_script="james.sh"
check_interval=10 # interval in seconds

start_service() {
    pids=$(ps aux | grep -v grep | grep "${service_script}" | awk '{print $2}')
    if [ -z "$pids" ]; then
	echo "Starting ${service_name}..."
        nohup ./${service_script} >/dev/null 2>&1 &
        echo "James service started successfully"
    else
    	echo "Service already running at this pid $pids"
    fi     
}

stop_service() {
    pids=$(ps aux | grep -v grep | grep "${service_script}" | awk '{print $2}')
    if [ -z "$pids" ]; then
        echo "${service_name} is not running."
    else
        echo "${service_name} is running with PIDs: $pids"
        echo "Stopping ${service_name}..."
        for pid in $pids; do
            kill -9 $pid
            echo "james service successfully stopped for pid $pid"
        done
    fi
}

status_service() {
    if ps aux | grep -v grep | grep -q "${service_script}"; then
        echo "${service_name} is running."
    else
        echo "${service_name} is not running."
    fi
}

check_service() {
    if ps aux | grep -v grep | grep -q "${service_script}"; then
        echo "${service_name} is running."
        return 0
    else
        echo "${service_name} is not running."
        return 1
    fi
}

get_pid() {
    pids=$(ps aux | grep -v grep | grep "${service_script}" | awk '{print $2}')
    if [ -z "$pids" ]; then
        echo "${service_name} is not running."
    else
        echo "${service_name} is running with PIDs: $pids"
    fi
}

continuous_check() {
    while true; do
        check_service
        sleep $check_interval
    done
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
    loop)
        continuous_check
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status|check|pid|loop}"
        exit 1
        ;;
esac

exit 0
