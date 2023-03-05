variable "region" {
  default = "eu-west-1"
}

variable "AWS_ACCESS_KEY_ID" {
  default = ""
}

variable "AWS_SECRET_ACCESS_KEY" {
  default = ""
}

variable "ping_pong_queue_name" {
  description = "queue name"
  type = string
  default = "PingPongMessages-Q"
}

variable "visibility_timeout" {
  description = "Time (in seconds) that consumers have to process a message before it becomes available again"
  type = number
  default = 20
}

variable "delivery_delay" {
  type = number
  default = 1
}

variable "retention_period" {
  description = "Time (in seconds) that messages will remain in queue before being purged"
  type = number
  default = 3600
}

variable "receive_wait_time_polling" {
  description = "Maximum amount of time that polling will wait for messages to become available to receive"
  type = number
  default = 1
}