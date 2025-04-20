folder('Marketflow') {
    displayName('Marketflow')
    description('CI/CD Jobs for Marketflow Project')
}

// Main
pipelineJob('Marketflow/marketflow-main') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/jicool91/test1.git')
                        credentials('github-access')
                    }
                    branch('main')
                }
            }
            scriptPath('Jenkinsfile.main')
        }
    }
}

// Backend
pipelineJob('Marketflow/marketflow-backend') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/jicool91/test1.git')
                        credentials('github-access')
                    }
                    branch('main')
                }
            }
            scriptPath('modules/ci/Jenkinsfile.backend')
        }
    }
}

// Frontend
pipelineJob('Marketflow/marketflow-frontend') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/jicool91/test1.git')
                        credentials('github-access')
                    }
                    branch('main')
                }
            }
            scriptPath('frontend/Jenkinsfile')
        }
    }
}

// Collect Metrics
pipelineJob('Marketflow/marketflow-collect-metrics') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/jicool91/test1.git')
                        credentials('github-access')
                    }
                    branch('main')
                }
            }
            scriptPath('modules/ci/Jenkinsfile.collect-metrics')
        }
    }
}

// Strategy Engine
pipelineJob('Marketflow/marketflow-strategy-engine') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/jicool91/test1.git')
                        credentials('github-access')
                    }
                    branch('main')
                }
            }
            scriptPath('modules/ci/Jenkinsfile.strategy-engine')
        }
    }
}

// PDF Generator
pipelineJob('Marketflow/marketflow-pdf-generator') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/jicool91/test1.git')
                        credentials('github-access')
                    }
                    branch('main')
                }
            }
            scriptPath('modules/ci/Jenkinsfile.pdf-generator')
        }
    }
}

// Bot Sender
pipelineJob('Marketflow/marketflow-bot-sender') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/jicool91/test1.git')
                        credentials('github-access')
                    }
                    branch('main')
                }
            }
            scriptPath('modules/ci/Jenkinsfile.bot-sender')
        }
    }
}