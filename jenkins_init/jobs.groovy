// Создаем основную джобу
pipelineJob('marketflow-main') {
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
    triggers {
        scm('H/5 * * * *')
    }
}

// Backend
pipelineJob('marketflow-backend') {
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
pipelineJob('marketflow-frontend') {
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
pipelineJob('marketflow-collect-metrics') {
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
pipelineJob('marketflow-strategy-engine') {
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
pipelineJob('marketflow-pdf-generator') {
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
pipelineJob('marketflow-bot-sender') {
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