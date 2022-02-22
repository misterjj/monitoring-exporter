import React from 'react'
import { AxiosResponse } from 'axios'

const axios = require('axios')

interface state {
  projects: ProjectImporter[]
}

interface ProjectImporter {
  url: string,
  hasError: boolean,
  project?: Project
}

interface Project {
  name: string,
  environments: Environment[]
}

interface Environment {
  name: string
  lastDeployment?: LastDeployment
}

enum DeploymentStatus {
  SUCCESS = 'success',
  FAILED = 'failed',
  RUNNING = 'running'
}

interface LastDeployment {
  ref: string
  status: DeploymentStatus
  user: User
}

interface User {
  avatar: string
  name: string
}

class Environments extends React.Component<any, state> {

  constructor (props: any) {
    super(props)
    this.state = {
      projects: []
    }
  }

  componentDidMount () {
    const projects = this.getProjectUrls()
    this.getProjects(projects)
    setInterval(() => {
      this.getProjects(projects)
    }, 10000)
  }



  deploymentStatusFromString(status: String): DeploymentStatus {
    switch (status) {
      case "success": return DeploymentStatus.SUCCESS
      case "running": return DeploymentStatus.RUNNING
      default: return DeploymentStatus.FAILED
    }
  }

  cardClassFromDeploymentStatus(status: DeploymentStatus|undefined): String {
    switch (status) {
      case DeploymentStatus.SUCCESS : return "bg-success"
      case DeploymentStatus.RUNNING : return "bg-warning"
      case DeploymentStatus.FAILED : return "bg-danger"
      case undefined : return "bg-danger"
    }
  }

  getProjects(projects: ProjectImporter[]) {
    Promise.all(projects.map(this.getProject.bind(this)))
      .then(projectImporter => this.setState({ ...this.state, projects: projectImporter }))
  }

  async getProject (projectImporter: ProjectImporter): Promise<ProjectImporter> {
    return axios
      .get(projectImporter.url)
      .then((response: AxiosResponse) => {
        return {
          ...projectImporter,
          project: {
            name: response.data.name,
            environments: response.data.environments.map((env: any) => (
              {
                name: env.name,
                lastDeployment: {
                  ref: env.lastDeployment.ref,
                  status: this.deploymentStatusFromString(env.lastDeployment.status),
                  user: {
                    avatar: env.lastDeployment.user.avatarUrl,
                    name: env.lastDeployment.user.name
                  }
                }
              }
            ))
          }
        }
      })
      .catch((error: any) => {
        console.log(error)
        return {
          ...projectImporter,
          hasError: true
        }
      })
  }

  getProjectUrls (): ProjectImporter[] {
    const params = new Proxy(new URLSearchParams(window.location.search), {
      get: (searchParams, prop: string) => searchParams.get(prop),
    })

    // @ts-ignore
    const urlsProject: string[] = params.projects.split(',')
    return urlsProject.map(url => ({ url, hasError: false }))
  }

  render () {
    return <div className="d-flex w-100 h-100 bg-dark text-white">
      {this.state.projects.map((projectImporter, indexP) => <div className="flex-fill p-3" key={'project-' + indexP}>
        {projectImporter.hasError && <div className="d-flex justify-content-center align-items-center h-100 ">
          <span className="text-danger">{projectImporter.url}</span>
        </div>}
        {!projectImporter.hasError && projectImporter.project !== null &&
        <div className="d-flex flex-column h-100">
          <h1 className="text-center w-100">{projectImporter.project?.name}</h1>
          {projectImporter.project?.environments.map((env, indexE) =>
              <div className={"card bg-secondary mb-3 " + this.cardClassFromDeploymentStatus(env.lastDeployment?.status)} key={'env-' + indexP + "-" + indexE}>
                <div className="card-header text-center">
                  <h3>{env.name}</h3>
                </div>
                <div className="card-body d-flex align-items-center">
                  <h4 className="flex-grow-1"><i className="fa-solid fa-code-branch"/> {env.lastDeployment?.ref}</h4>
                  <div>{env.lastDeployment?.user.name}</div>
                  <img src={env.lastDeployment?.user.avatar} height="30px" alt="" className="rounded-circle ms-1"/>
                </div>
              </div>
          )
          }
        </div>
        }
      </div>)}
    </div>
  }
}

export default Environments
