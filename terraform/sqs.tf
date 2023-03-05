resource "aws_sqs_queue" "sqs_ping_pong_queue" {
  name                       = var.ping_pong_queue_name
  visibility_timeout_seconds = var.visibility_timeout
  delay_seconds              = var.delivery_delay
  message_retention_seconds  = var.retention_period
  receive_wait_time_seconds  = var.receive_wait_time_polling
  sqs_managed_sse_enabled    = false
}

resource "aws_sqs_queue_policy" "sqs_queue_policy" {
  queue_url = aws_sqs_queue.sqs_ping_pong_queue.id
  policy    = <<POLICY
{
  "Version": "2008-10-17",
  "Id": "__default_policy_ID",
  "Statement": [
    {
      "Sid": "__owner_statement",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::028716807356:root"
      },
      "Action": "SQS:*",
      "Resource": "${aws_sqs_queue.sqs_ping_pong_queue.arn}"
    }
  ]
}
POLICY
}